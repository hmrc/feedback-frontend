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
import forms.NmwCcgQuestionsFormProvider
import generators.ModelGenerators
import models.{FeedbackId, NmwCcgQuestions, Origin}
import navigation.FakeNavigator
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{reset, times, verify}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.data.Form
import play.api.mvc.Call
import play.api.test.Helpers._
import services.AuditService
import views.html.NmwCcgQuestionsView

class NmwCcgQuestionsControllerSpec
    extends SpecBase with ScalaCheckPropertyChecks with ModelGenerators with MockitoSugar {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new NmwCcgQuestionsFormProvider()
  val form = formProvider()
  lazy val nmwCcgQuestionsView = inject[NmwCcgQuestionsView]
  def submitCall(origin: Origin) = routes.NmwCcgQuestionsController.onSubmit(origin)
  def viewAsString(form: Form[_] = form, action: Call) =
    nmwCcgQuestionsView(frontendAppConfig, form, action)(fakeRequest, messages).toString
  lazy val mockAuditService = mock[AuditService]

  val controller = new NmwCcgQuestionsController(
    frontendAppConfig,
    mcc,
    nmwCcgQuestionsView,
    formProvider,
    mockAuditService,
    new FakeNavigator(onwardRoute)
  )

  "NmwCcgQuestions Controller" must {

    "return OK and the correct view for a GET" in {

      forAll (Gen.alphaStr) { str =>
        val origin = Origin.fromString(str)
        val result = controller.onPageLoad(origin)(fakeRequest)

        status(result) mustBe OK
        contentAsString(result) mustBe viewAsString(action = submitCall(origin))
      }
    }
  }

  "redirect to the next page when valid data is submitted" in {

    forAll(Gen.alphaStr) { str =>
      val origin = Origin.fromString(str)
      val result = controller.onSubmit(origin)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)

    }
  }

  "audit response on success" in {
    forAll(Gen.alphaStr, arbitrary[FeedbackId], arbitrary[NmwCcgQuestions]) {
      (originStr, feedbackId, answers) =>
        reset(mockAuditService)
        val origin = Origin.fromString(originStr)
        val values = Map(
          "treatedProfessionally" -> answers.treatedProfessionally.map(_.toString),
          "checkUnderstanding"    -> answers.checkUnderstanding.map(_.toString),
          "whyGiveAnswer"         -> answers.whyGiveAnswer,
          "supportFutureNmw"      -> answers.supportFutureNmw.map(_.toString)
        ).map(value => (value._1, value._2.getOrElse(""))).toSeq

        val request = fakeRequest
          .withMethod("POST")
          .withFormUrlEncodedBody(values: _*)
        val result = controller.onSubmit(origin)(request.withSession(("feedbackId", feedbackId.value)))
        status(result) mustBe SEE_OTHER

        verify(mockAuditService, times(1))
          .nmwCcgAudit(eqTo(origin), eqTo(feedbackId), eqTo(answers))(any())
    }
  }

  "return a Bad Request and errors when invalid data is submitted" in {
    forAll(Gen.alphaStr) { str =>
      val origin = Origin.fromString(str)
      val invalidValue = "*" * 1001
      val postRequest = fakeRequest
        .withMethod("POST")
        .withFormUrlEncodedBody(("whyGiveAnswer", invalidValue))
      val boundForm = form.bind(Map("whyGiveAnswer" -> invalidValue))
      val viewAsString =
        nmwCcgQuestionsView(frontendAppConfig, boundForm, submitCall(origin))(fakeRequest, messages).toString
      val result = controller.onSubmit(origin)(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString

    }
  }
}
