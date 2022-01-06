/*
 * Copyright 2022 HM Revenue & Customs
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
import forms.GiveCommentsFormProvider
import generators.ModelGenerators
import models.{FeedbackId, Origin}
import navigation.FakeNavigator
import org.mockito.Matchers.{eq => eqTo, _}
import org.mockito.Mockito._
import org.scalacheck.Arbitrary._
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.data.Form
import play.api.mvc.Call
import play.api.test.Helpers._
import services.AuditService
import views.html.GiveCommentsView

class GiveCommentsControllerSpec
    extends SpecBase with ScalaCheckPropertyChecks with ModelGenerators with MockitoSugar with ScalaFutures {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new GiveCommentsFormProvider()
  val form = formProvider()
  lazy val mockAuditService = mock[AuditService]
  lazy val giveCommentsView = inject[GiveCommentsView]

  def submitCall(origin: Origin) = routes.GiveCommentsController.onSubmit(origin)

  def controller() =
    new GiveCommentsController(
      frontendAppConfig,
      new FakeNavigator(onwardRoute),
      formProvider,
      mockAuditService,
      mcc,
      giveCommentsView)

  def viewAsString(form: Form[_] = form, action: Call) =
    giveCommentsView(frontendAppConfig, form, action)(fakeRequest, messages).toString

  "GiveComments Controller" must {

    "return OK and the correct view for a GET" in {
      forAll(arbitrary[Origin]) { origin =>
        val result = controller().onPageLoad(origin)(fakeRequest)

        status(result) mustBe OK
        contentAsString(result) mustBe viewAsString(action = submitCall(origin))
      }
    }

    "redirect to the next page when valid data is submitted" in {
      forAll(arbitrary[Origin]) { origin =>
        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "value"))
        val result = controller().onSubmit(origin)(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(onwardRoute.url)
      }
    }

    "audit response on success" in {
      forAll(arbitrary[Origin], arbitrary[FeedbackId], arbitrary[String]) { (origin, feedbackId, answer) =>
        reset(mockAuditService)

        val values = Map("value" -> answer)

        val request = fakeRequest.withFormUrlEncodedBody(values.toList: _*)
        controller().onSubmit(origin)(request.withSession(("feedbackId", feedbackId.value))).futureValue

        verify(mockAuditService, times(1))
          .giveCommentsAudit(eqTo(origin), eqTo(feedbackId), eqTo(answer))(any())
      }
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      forAll(arbitrary[Origin]) { origin =>
        val invalidValue = "*" * 1001
        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", invalidValue))
        val boundForm = form.bind(Map("value" -> invalidValue))

        val result = controller().onSubmit(origin)(postRequest)

        status(result) mustBe BAD_REQUEST
        contentAsString(result) mustBe viewAsString(form = boundForm, action = submitCall(origin))
      }
    }
  }
}
