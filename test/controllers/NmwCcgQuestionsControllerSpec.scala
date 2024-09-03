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
import forms.NmwCcgQuestionsFormProvider
import models.ccg.{CheckUnderstandingQuestion, SupportFutureQuestion, TreatedProfessionallyQuestion}
import models.{FeedbackId, NmwCcgQuestions, Origin}
import navigation.FakeNavigator
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{reset, times, verify}
import org.scalatestplus.mockito.MockitoSugar
import play.api.data.Form
import play.api.mvc.Call
import play.api.test.Helpers._
import services.AuditService
import views.html.NmwCcgQuestionsView

import scala.util.Random

class NmwCcgQuestionsControllerSpec extends SpecBase with MockitoSugar {

  lazy val nmwCcgQuestionsView: NmwCcgQuestionsView = inject[NmwCcgQuestionsView]
  lazy val mockAuditService: AuditService = mock[AuditService]

  val formProvider                                  = new NmwCcgQuestionsFormProvider()
  val form: Form[NmwCcgQuestions]                   = formProvider()
  val controller = new NmwCcgQuestionsController(
    frontendAppConfig,
    mcc,
    nmwCcgQuestionsView,
    formProvider,
    mockAuditService,
    new FakeNavigator(onwardRoute)
  )

  def onwardRoute: Call = Call("GET", "/foo")

  def submitCall(origin: Origin): Call = routes.NmwCcgQuestionsController.onSubmit(origin)

  def viewAsString(form: Form[_] = form, action: Call): String =
    nmwCcgQuestionsView(frontendAppConfig, form, action)(fakeRequest, messages).toString

  "NmwCcgQuestions Controller" must {

    "return OK and the correct view for a GET" in {
      for (serviceName <- serviceNames) {
        val origin = Origin.fromString(serviceName)
        val result = controller.onPageLoad(origin)(fakeRequest)

        status(result) mustBe OK
        contentAsString(result) mustBe viewAsString(action = submitCall(origin))
      }
    }
  }

  "redirect to the next page when valid data is submitted" in {
    for (serviceName <- serviceNames) {
      val origin = Origin.fromString(serviceName)
      val result = controller.onSubmit(origin)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)

    }
  }

  "audit response on success" in {
    for (serviceName <- serviceNames) {
      reset(mockAuditService)
      val origin = Origin.fromString(serviceName)

      val haveYouBeenTreatedProfessionally                  =
        Some(TreatedProfessionallyQuestion.values(Random.nextInt(treatedProfessionallyQuestionNumberOfOptions)))
      val howEasyWasItToUnderstandWhatWasHappening          =
        Some(CheckUnderstandingQuestion.values(Random.nextInt(checkUnderstandingQuestionNumberOfOptions)))
      val whyDidYouGiveThisAnswer                           = Some("Because I felt like giving this answer !")
      val willYourHMRCInteractionsHelpYouMeetTaxObligations =
        Some(SupportFutureQuestion.values(Random.nextInt(supportFutureQuestionNumberOfOptions)))

      val answers = NmwCcgQuestions(
        haveYouBeenTreatedProfessionally,
        howEasyWasItToUnderstandWhatWasHappening,
        whyDidYouGiveThisAnswer,
        willYourHMRCInteractionsHelpYouMeetTaxObligations
      )

      val values = Map(
        "treatedProfessionally" -> answers.treatedProfessionally.map(_.toString),
        "checkUnderstanding"    -> answers.checkUnderstanding.map(_.toString),
        "whyGiveAnswer"         -> answers.whyGiveAnswer,
        "supportFutureNmw"      -> answers.supportFutureNmw.map(_.toString)
      ).map(value => (value._1, value._2.getOrElse(""))).toSeq

      val request = fakeRequest
        .withMethod("POST")
        .withFormUrlEncodedBody(values: _*)

      val feedbackId = FeedbackId.fromSession(request)

      val result = controller.onSubmit(origin)(request.withSession(("feedbackId", feedbackId.value)))
      status(result) mustBe SEE_OTHER

      verify(mockAuditService, times(1))
        .nmwCcgAudit(eqTo(origin), eqTo(feedbackId), eqTo(answers))(any())
    }
  }

  "return a Bad Request and errors when invalid data is submitted" in {
    for (serviceName <- serviceNames) {
      val origin       = Origin.fromString(serviceName)
      val invalidValue = "*" * 1001
      val postRequest  = fakeRequest
        .withMethod("POST")
        .withFormUrlEncodedBody(("whyGiveAnswer", invalidValue))
      val boundForm    = form.bind(Map("whyGiveAnswer" -> invalidValue))
      val viewAsString =
        nmwCcgQuestionsView(frontendAppConfig, boundForm, submitCall(origin))(fakeRequest, messages).toString
      val result       = controller.onSubmit(origin)(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString

    }
  }
}
