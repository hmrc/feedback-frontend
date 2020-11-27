/*
 * Copyright 2020 HM Revenue & Customs
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

import forms.CCGQuestionsFormProvider
import views.html.ccgQuestions
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

class CCGQuestionsControllerSpec
    extends ControllerSpecBase with ScalaCheckPropertyChecks with ModelGenerators with MockitoSugar {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new CCGQuestionsFormProvider()
  val form = formProvider()
  lazy val mockAuditService = mock[AuditService]

  def submitCall() = routes.CCGQuestionsController.onSubmit()

  def controller() =
    new CCGQuestionsController(frontendAppConfig, new FakeNavigator(onwardRoute), formProvider, mockAuditService, mcc)

  def viewAsString(form: Form[_] = form, action: Call) =
    ccgQuestions(frontendAppConfig, form, action)(fakeRequest, messages).toString

  "CCGQuestions Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad()(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString(action = submitCall())
    }

    "redirect to the next page when valid data is submitted" in {
      val result = controller().onSubmit()(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "audit response on success" in {
      forAll(arbitrary[FeedbackId], arbitrary[CCGQuestions]) { (feedbackId, answers) =>
        reset(mockAuditService)

        val values = form.mapping.unbind(answers)

        val request = fakeRequest.withFormUrlEncodedBody(values.toSeq: _*)

        controller().onSubmit(request.withSession(("feedbackId", feedbackId.value)))

        verify(mockAuditService, times(1))
          .ccgAudit(eqTo(feedbackId), eqTo(answers))(any())
      }
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      forAll(arbitrary[Origin]) { origin =>
        val invalidValue = "*" * 1001
        val postRequest = fakeRequest.withFormUrlEncodedBody(("whyGiveAnswer", invalidValue))
        val boundForm = form.bind(Map("whyGiveAnswer" -> invalidValue))

        val result = controller().onSubmit()(postRequest)

        status(result) mustBe BAD_REQUEST
        contentAsString(result) mustBe viewAsString(form = boundForm, action = submitCall())
      }
    }
  }
}
