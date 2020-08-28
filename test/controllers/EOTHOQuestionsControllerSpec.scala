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

import controllers.actions._
import forms.EOTHOQuestionsFormProvider
import generators.ModelGenerators
import models.{EOTHOQuestions, FeedbackId, Origin}
import navigation.FakeNavigator
import org.mockito.Matchers.{eq => eqTo, _}
import org.mockito.Mockito._
import org.scalacheck.Arbitrary._
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.data.Form
import play.api.mvc.Call
import play.api.test.Helpers._
import services.AuditService
import views.html.eothoQuestions

class EOTHOQuestionsControllerSpec
    extends ControllerSpecBase with ScalaCheckPropertyChecks with ModelGenerators with MockitoSugar {

  val onwardRoute = Call("GET", "/foo")

  val formProvider = new EOTHOQuestionsFormProvider()
  val form = formProvider()

  val mockAuditService = mock[AuditService]

  def submitCall = routes.EOTHOQuestionsController.onSubmit

  def controller() =
    new EOTHOQuestionsController(frontendAppConfig, new FakeNavigator(onwardRoute), formProvider, mockAuditService, mcc)

  def viewAsString(form: Form[_] = form, action: Call) =
    eothoQuestions(frontendAppConfig, form, action)(fakeRequest, messages).toString

  "EOTHOQuestions Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString(action = submitCall)
    }

    "redirect to the next page when valid data is submitted" in {
      val result = controller().onSubmit(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "audit response on success" in {
      forAll(arbitrary[FeedbackId], arbitrary[EOTHOQuestions]) { (feedbackId, answers) =>
        reset(mockAuditService)

        val values = form.mapping.unbind(answers)

        val request = fakeRequest.withFormUrlEncodedBody(values.toSeq: _*)

        controller().onSubmit(request.withSession(("feedbackId", feedbackId.value)))

        verify(mockAuditService, times(1))
          .eothoAudit(eqTo(feedbackId), eqTo(answers))(any())
      }
    }
  }
}
