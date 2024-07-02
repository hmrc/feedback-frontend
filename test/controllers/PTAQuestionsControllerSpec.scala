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
import forms.PTAQuestionsFormProvider
import generators.ModelGenerators
import models.{FeedbackId, Origin, PTAQuestions}
import navigation.FakeNavigator
import org.mockito.ArgumentMatchers.{eq => eqTo, _}
import org.mockito.Mockito._
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.data.Form
import play.api.mvc.Call
import play.api.test.Helpers._
import services.AuditService
import views.html.PtaQuestionsView

class PTAQuestionsControllerSpec extends SpecBase with ScalaCheckPropertyChecks with ModelGenerators with MockitoSugar {

  def onwardRoute: Call = Call("GET", "/foo")

  val formProvider = new PTAQuestionsFormProvider()
  val form: Form[PTAQuestions] = formProvider()

  lazy val mockAuditService: AuditService = mock[AuditService]
  lazy val ptaQuestionsView: PtaQuestionsView = inject[PtaQuestionsView]

  def submitCall(origin: Origin): Call = routes.PTAQuestionsController.onSubmit(origin)

  def controller() =
    new PTAQuestionsController(
      frontendAppConfig,
      new FakeNavigator(onwardRoute),
      formProvider,
      mockAuditService,
      mcc,
      ptaQuestionsView)

  def viewAsString(form: Form[_] = form, action: Call): String =
    ptaQuestionsView(frontendAppConfig, form, action)(fakeRequest, messages).toString

  "PTAQuestions Controller" must {

    "return OK and the correct view for a GET" in {
      forAll (Gen.alphaStr) { str =>
        val origin = Origin.fromString(str)
        val result = controller().onPageLoad(origin)(fakeRequest)

        status(result) mustBe OK
        contentAsString(result) mustBe viewAsString(action = submitCall(origin))
      }
    }

    "redirect to the next page when valid data is submitted" in {
      forAll (Gen.alphaStr) { str =>
        val origin = Origin.fromString(str)
        val result = controller().onSubmit(origin)(fakeRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(onwardRoute.url)
      }
    }

    "audit response on success" in {
      forAll(Gen.alphaStr, arbitrary[FeedbackId], arbitrary[PTAQuestions]) { (originStr, feedbackId, answers) =>
        reset(mockAuditService)
        val origin = Origin.fromString(originStr)
        val values = Map(
          "neededToDo"        -> answers.neededToDo,
          "ableToDo"          -> answers.ableToDo.map(_.toString),
          "howEasyScore"      -> answers.howEasyScore.map(_.toString),
          "whyGiveScore"      -> answers.whyGiveScore,
          "howDoYouFeelScore" -> answers.howDoYouFeelScore.map(_.toString)
        ).map(value => (value._1, value._2.getOrElse(""))).toSeq

        val request = fakeRequest
          .withMethod("POST")
          .withFormUrlEncodedBody(values: _*)
        controller().onSubmit(origin)(request.withSession(("feedbackId", feedbackId.value)))

        verify(mockAuditService, times(1))
          .ptaAudit(eqTo(origin), eqTo(feedbackId), eqTo(answers))(any())
      }
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      forAll (Gen.alphaStr) { str =>
        val origin = Origin.fromString(str)
        val postRequest = fakeRequest
          .withMethod("POST")
          .withFormUrlEncodedBody(("ableToDo", "invalid value"))
        val boundForm = form.bind(Map("ableToDo" -> "invalid value"))

        val result = controller().onSubmit(origin)(postRequest)

        status(result) mustBe BAD_REQUEST
        contentAsString(result) mustBe viewAsString(form = boundForm, action = submitCall(origin))
      }
    }
  }
}
