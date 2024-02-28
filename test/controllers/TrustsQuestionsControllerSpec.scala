/*
 * Copyright 2023 HM Revenue & Customs
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
import forms.TrustsQuestionsFormProvider
import generators.ModelGenerators
import models.{FeedbackId, Origin, TrustsQuestions}
import navigation.FakeNavigator
import org.mockito.ArgumentMatchers.{eq => eqTo, _}
import org.mockito.Mockito._
import org.scalacheck.Arbitrary._
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.data.Form
import play.api.mvc.Call
import play.api.test.Helpers._
import services.AuditService
import views.html.TrustsQuestionsView

class TrustsQuestionsControllerSpec
    extends SpecBase with ScalaCheckPropertyChecks with ModelGenerators with MockitoSugar with ScalaFutures {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new TrustsQuestionsFormProvider()
  val form = formProvider()
  lazy val mockAuditService = mock[AuditService]

  lazy val submitCall: Call = routes.TrustsQuestionsController.onSubmit()

  def controller() =
    new TrustsQuestionsController(
      frontendAppConfig,
      new FakeNavigator(onwardRoute),
      formProvider,
      mockAuditService,
      mcc,
      inject[TrustsQuestionsView]
    )

  lazy val trustsQuestions = inject[TrustsQuestionsView]

  def viewAsString(form: Form[_] = form) =
    trustsQuestions(frontendAppConfig, form, submitCall)(fakeRequest, messages).toString

  "TrustsQuestions Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad()(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "redirect to the next page when valid data is submitted" in {
      val result = controller().onSubmit()(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "audit response on success" in {
      forAll(arbitrary[FeedbackId], arbitrary[TrustsQuestions]) { (feedbackId, answers) =>
        reset(mockAuditService)

        val values = Map(
          "isAgent"           -> answers.isAgent.map(_.toString),
          "tryingToDo"        -> answers.tryingToDo.map(_.toString),
          "tryingToDoOther"   -> answers.tryingToDoOther,
          "ableToDo"          -> answers.ableToDo.map(_.toString),
          "whyNotAbleToDo"    -> answers.whyNotAbleToDo,
          "howEasyScore"      -> answers.howEasyScore.map(_.toString),
          "whyGiveScore"      -> answers.whyGiveScore,
          "howDoYouFeelScore" -> answers.howDoYouFeelScore.map(_.toString)
        ).map(value => (value._1, value._2.getOrElse(""))).toSeq

        val request = fakeRequest
          .withMethod("POST")
          .withFormUrlEncodedBody(values: _*)

        val result = controller().onSubmit()(request.withSession(("feedbackId", feedbackId.value)))
        status(result) mustBe SEE_OTHER

        verify(mockAuditService, times(1))
          .trustsAudit(eqTo(Origin.fromString("trusts")), eqTo(feedbackId), eqTo(answers))(any())
      }
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest
        .withMethod("POST")
        .withFormUrlEncodedBody(("ableToDo", "invalid value"))
      val boundForm = form.bind(Map("ableToDo" -> "invalid value"))

      val result = controller().onSubmit()(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(form = boundForm)
    }
  }
}
