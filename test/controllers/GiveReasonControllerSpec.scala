/*
 * Copyright 2019 HM Revenue & Customs
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

import connectors.FakeDataCacheConnector
import controllers.actions._
import forms.GiveReasonFormProvider
import models.{GiveReason, GiveReasonQuestions}
import navigation.FakeNavigator
import org.mockito.Matchers.any
import org.mockito.Mockito.{reset, times, verify}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.prop.PropertyChecks
import play.api.data.Form
import play.api.mvc.Call
import play.api.test.Helpers._
import views.html.giveReason

class GiveReasonControllerSpec extends ControllerSpecBase with PropertyChecks {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new GiveReasonFormProvider()
  val form = formProvider()

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new GiveReasonController(frontendAppConfig, messagesApi, FakeDataCacheConnector, new FakeNavigator(onwardRoute), FakeIdentifierAction,
      dataRetrievalAction, new DataRequiredActionImpl, formProvider)

  def viewAsString(form: Form[_] = form, origin: String = "") =
    giveReason(frontendAppConfig, form, routes.GiveReasonController.onSubmit(origin))(fakeRequest, messages).toString

  "GiveReason Controller" must {

    "return OK and the correct view for a GET" in {

      forAll { origin: String =>

        val result = controller().onPageLoad(origin)(fakeRequest)

        status(result) mustBe OK
        contentAsString(result) mustBe viewAsString(origin = origin)
      }
    }

    "redirect to the next page when valid data is submitted" in {

      forAll { origin: String =>

        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", GiveReason.options.head.value))

        val result = controller().onSubmit(origin)(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(onwardRoute.url)
      }
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      forAll { origin: String =>

        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
        val boundForm = form.bind(Map("value" -> "invalid value"))

        val result = controller().onSubmit(origin)(postRequest)

        status(result) mustBe BAD_REQUEST
        contentAsString(result) mustBe viewAsString(boundForm, origin)
      }
    }

    "audit response on success" in {
      forAll(arbitrary[String], arbitrary[String], arbitrary[GiveReasonQuestions]) {
        (origin, feedbackId, answers) =>
          reset(mockAuditService)

          val values = Map(
            "value" -> answers.ableToDo.map(_.toString),
            "reason" -> answers.howEasyScore.map(_.toString),
            "whyGiveScore" -> answers.whyGiveScore,
            "howDoYouFeelScore" -> answers.howDoYouFeelScore.map(_.toString),
            "likelyToDo" -> answers.likelyToDo.map(_.toString))

          val request = fakeRequest.withFormUrlEncodedBody(values.mapValues(_.getOrElse("")).toList: _*)
          controller().onSubmit(origin)(request.withSession(("feedbackId", feedbackId)))

          verify(mockAuditService, times(1))
            .pensionAudit(eqTo(origin), eqTo(feedbackId), eqTo(answers))(any())
      }
    }
  }
}
