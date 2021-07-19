/*
 * Copyright 2021 HM Revenue & Customs
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
import forms.CCGQuestionsFormProvider
import views.html.CcgQuestionsView
import generators.ModelGenerators
import models.{CCGQuestions, FeedbackId, Origin}
import navigation.FakeNavigator
import org.mockito.Matchers.any
import org.mockito.Mockito.{reset, times, verify}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.data.Form
import play.api.mvc.Call
import play.api.test.Helpers._
import services.AuditService
import org.mockito.Matchers.{eq => eqTo, _}

class CCGQuestionsControllerSpec extends SpecBase with ScalaCheckPropertyChecks with ModelGenerators with MockitoSugar {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new CCGQuestionsFormProvider()
  val form = formProvider()
  lazy val mockAuditService = mock[AuditService]
  lazy val ccgQuestionsView = inject[CcgQuestionsView]

  def submitCall(origin: Origin) = routes.CCGQuestionsController.onSubmit(origin)

  def controller() =
    new CCGQuestionsController(
      frontendAppConfig,
      new FakeNavigator(onwardRoute),
      formProvider,
      mockAuditService,
      mcc,
      ccgQuestionsView)

  def viewAsString(form: Form[_] = form, action: Call) =
    ccgQuestionsView(frontendAppConfig, form, action)(fakeRequest, messages).toString

  "CCGQuestions Controller" must {

    "return OK and the correct view for a GET" in {
      forAll(arbitrary[Origin]) { origin =>
        val result = controller().onPageLoad(origin)(fakeRequest)

        status(result) mustBe OK
        contentAsString(result) mustBe viewAsString(action = submitCall(origin))
      }
    }

    "redirect to the next page when valid data is submitted" in {
      forAll(arbitrary[Origin]) { origin =>
        val result = controller().onSubmit(origin)(fakeRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(onwardRoute.url)
      }
    }

    "audit response on success" in {
      forAll(arbitrary[Origin], arbitrary[FeedbackId], arbitrary[CCGQuestions]) { (origin, feedbackId, answers) =>
        reset(mockAuditService)

        val values = Map(
          "complianceCheckUnderstanding" -> answers.complianceCheckUnderstanding.map(_.toString),
          "treatedProfessionally"        -> answers.treatedProfessionally.map(_.toString),
          "whyGiveAnswer"                -> answers.whyGiveAnswer,
          "supportFutureTax"             -> answers.supportFutureTaxQuestion.map(_.toString)
        )

        val request = fakeRequest.withFormUrlEncodedBody(values.mapValues(_.getOrElse("")).toList: _*)
        val result = controller().onSubmit(origin)(request.withSession(("feedbackId", feedbackId.value)))
        status(result) mustBe SEE_OTHER

        verify(mockAuditService, times(1))
          .ccgAudit(eqTo(origin), eqTo(feedbackId), eqTo(answers))(any())
      }
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      forAll(arbitrary[Origin]) { origin =>
        val invalidValue = "*" * 1001
        val postRequest = fakeRequest.withFormUrlEncodedBody(("whyGiveAnswer", invalidValue))
        val boundForm = form.bind(Map("whyGiveAnswer" -> invalidValue))

        val result = controller().onSubmit(origin)(postRequest)

        status(result) mustBe BAD_REQUEST
        contentAsString(result) mustBe viewAsString(form = boundForm, action = submitCall(origin))
      }
    }
  }
}
