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
import forms.GiveReasonFormProvider
import generators.Generators
import models.{FeedbackId, GiveReason, GiveReasonQuestions, Origin}
import navigation.FakeNavigator
import org.mockito.Matchers.{any, eq => eqTo}
import org.mockito.Mockito.{reset, times, verify}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.mockito.MockitoSugar
import org.scalatest.prop.PropertyChecks
import play.api.data.Form
import play.api.mvc.Call
import play.api.test.Helpers._
import services.AuditService
import views.html.giveReason

class GiveReasonControllerSpec extends ControllerSpecBase with PropertyChecks with Generators with MockitoSugar {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new GiveReasonFormProvider()
  val form = formProvider()
  lazy val mockAuditService = mock[AuditService]

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new GiveReasonController(frontendAppConfig, new FakeNavigator(onwardRoute), formProvider, mockAuditService, mcc)

  def viewAsString(form: Form[_] = form, origin: Origin) =
    giveReason(frontendAppConfig, form, routes.GiveReasonController.onSubmit(origin))(fakeRequest, messages).toString

  "GiveReason Controller" must {

    "return OK and the correct view for a GET" in {

      forAll { origin: Origin =>
        val result = controller().onPageLoad(origin)(fakeRequest)

        status(result) mustBe OK
        contentAsString(result) mustBe viewAsString(origin = origin)
      }
    }

    "redirect to the next page when valid data is submitted" in {

      forAll { origin: Origin =>
        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", GiveReason.options.head.value))

        val result = controller().onSubmit(origin)(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(onwardRoute.url)
      }
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      forAll { origin: Origin =>
        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
        val boundForm = form.bind(Map("value" -> "invalid value"))

        val result = controller().onSubmit(origin)(postRequest)

        status(result) mustBe BAD_REQUEST
        contentAsString(result) mustBe viewAsString(boundForm, origin)
      }
    }

    "audit response on success" in {
      forAll(arbitrary[Origin], arbitrary[FeedbackId], arbitrary[GiveReasonQuestions]) {
        (origin, feedbackId, answers) =>
          reset(mockAuditService)

          val values = Map(
            "value"  -> answers.value.map(_.toString),
            "reason" -> answers.reason
          )

          val request = fakeRequest.withFormUrlEncodedBody(values.mapValues(_.getOrElse("")).toList: _*)
          controller().onSubmit(origin)(request.withSession(("feedbackId", feedbackId.value)))

          verify(mockAuditService, times(1))
            .giveReasonAudit(eqTo(origin), eqTo(feedbackId), eqTo(answers))(any())
      }
    }
  }
}
