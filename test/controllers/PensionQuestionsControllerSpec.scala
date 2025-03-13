/*
 * Copyright 2024 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers

import base.SpecBase
import base.CommonSpecValues._
import forms.PensionQuestionsFormProvider
import models._
import navigation.FakeNavigator
import org.mockito.ArgumentMatchers.{eq => eqTo, _}
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import play.api.data.Form
import play.api.mvc.Call
import play.api.test.Helpers._
import services.AuditService
import views.html.PensionQuestionsView

import scala.util.Random

class PensionQuestionsControllerSpec extends SpecBase with MockitoSugar {

  lazy val mockAuditService: AuditService             = mock[AuditService]
  lazy val pensionQuestionsView: PensionQuestionsView = inject[PensionQuestionsView]

  val formProvider                                    = new PensionQuestionsFormProvider()
  val form: Form[PensionQuestions]                    = formProvider()

  def submitCall(origin: Origin): Call = routes.PensionQuestionsController.onSubmit(origin)

  def controller() =
    new PensionQuestionsController(
      frontendAppConfig,
      new FakeNavigator(onwardRoute),
      formProvider,
      mockAuditService,
      mcc,
      pensionQuestionsView
    )

  def onwardRoute: Call = Call("GET", "/foo")

  def viewAsString(form: Form[?] = form, action: Call): String =
    pensionQuestionsView(frontendAppConfig, form, action)(fakeRequest, messages).toString

  "PensionQuestions Controller" must {

    "return OK and the correct view for a GET" in {
      for (serviceName <- serviceNames) {
        val origin = Origin.fromString(serviceName)
        val result = controller().onPageLoad(origin)(fakeRequest)

        status(result) mustBe OK
        contentAsString(result) mustBe viewAsString(action = submitCall(origin))
      }
    }

    "redirect to the next page when valid data is submitted" in {
      for (serviceName <- serviceNames) {
        val origin = Origin.fromString(serviceName)
        val result = controller().onSubmit(origin)(fakeRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(onwardRoute.url)
      }
    }

    "audit response on success" in {
      for (serviceName <- serviceNames) {
        reset(mockAuditService)
        val origin = Origin.fromString(serviceName)

        val wereYouAbleToDoWhatYouWant                 = Some(AbleToDo.values(Random.nextInt(ableToDoQuestionNumberOfOptions)))
        val howEasyWasItToDoYourTask                   = Some(HowEasyQuestion.values(Random.nextInt(howEasyQuestionNumberOfOptions)))
        val whyDidYouGiveThisScore                     = Some("Because I felt like giving this score !")
        val howDoYouFeelAboutTheService                =
          Some(HowDoYouFeelQuestion.values(Random.nextInt(howDoYouFeelQuestionNumberOfOptions)))
        val whatWillYouDoAfterCheckingYourStatePension =
          Some(LikelyToDoQuestion.values(Random.nextInt(likelyToDoQuestionNumberOfOptions)))

        val answers = PensionQuestions(
          wereYouAbleToDoWhatYouWant,
          howEasyWasItToDoYourTask,
          whyDidYouGiveThisScore,
          howDoYouFeelAboutTheService,
          whatWillYouDoAfterCheckingYourStatePension
        )

        val values = Map(
          "ableToDo"          -> answers.ableToDo.map(_.toString),
          "howEasyScore"      -> answers.howEasyScore.map(_.toString),
          "whyGiveScore"      -> answers.whyGiveScore,
          "howDoYouFeelScore" -> answers.howDoYouFeelScore.map(_.toString),
          "likelyToDo"        -> answers.likelyToDo.map(_.toString)
        ).map(value => (value._1, value._2.getOrElse(""))).toSeq

        val request = fakeRequest
          .withMethod("POST")
          .withFormUrlEncodedBody(values *)

        val feedbackId = FeedbackId.fromSession(request)

        controller().onSubmit(origin)(request.withSession(("feedbackId", feedbackId.value)))

        verify(mockAuditService, times(1))
          .pensionAudit(eqTo(origin), eqTo(feedbackId), eqTo(answers))(any())
      }
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      for (serviceName <- serviceNames) {
        val origin      = Origin.fromString(serviceName)
        val postRequest = fakeRequest
          .withMethod("POST")
          .withFormUrlEncodedBody(("ableToDo", "invalid value"))
        val boundForm   = form.bind(Map("ableToDo" -> "invalid value"))

        val result = controller().onSubmit(origin)(postRequest)

        status(result) mustBe BAD_REQUEST
        contentAsString(result) mustBe viewAsString(form = boundForm, action = submitCall(origin))
      }
    }
  }
}
