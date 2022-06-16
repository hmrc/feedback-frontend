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
import forms.OtherQuestionsFormProvider
import generators.ModelGenerators
import models.{Cid, FeedbackId, Origin, OtherQuestions}
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
import views.html.OtherQuestionsView

class OtherQuestionsControllerSpec
    extends SpecBase with ScalaCheckPropertyChecks with ModelGenerators with MockitoSugar {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new OtherQuestionsFormProvider()
  val form = formProvider()
  lazy val mockAuditService = mock[AuditService]
  lazy val otherQuestionsView = inject[OtherQuestionsView]

  def submitCall(origin: Origin) = routes.OtherQuestionsController.onSubmit(origin)

  def controller() =
    new OtherQuestionsController(
      frontendAppConfig,
      new FakeNavigator(onwardRoute),
      formProvider,
      mockAuditService,
      mcc,
      otherQuestionsView)

  def viewAsString(form: Form[_] = form, action: Call) =
    otherQuestionsView(frontendAppConfig, form, action)(fakeRequest, messages).toString

  "OtherQuestions Controller" must {

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
      forAll(arbitrary[Origin], arbitrary[FeedbackId], arbitrary[OtherQuestions], arbitrary[Cid]) {
        (origin, feedbackId, answers, cid) =>
          reset(mockAuditService)

          val values = Map(
            "ableToDo"          -> answers.ableToDo.map(_.toString),
            "howEasyScore"      -> answers.howEasyScore.map(_.toString),
            "whyGiveScore"      -> answers.whyGiveScore,
            "howDoYouFeelScore" -> answers.howDoYouFeelScore.map(_.toString)
          )

          val request = fakeRequest
            .withFormUrlEncodedBody(values.mapValues(_.getOrElse("")).toList: _*)
            .withSession(("feedbackId", feedbackId.value))
            .withHeaders("referer" -> s"/feedback/EXAMPLE?cid=${cid.value}")

          controller().onSubmit(origin)(request)

          verify(mockAuditService, times(1))
            .otherAudit(eqTo(origin), eqTo(feedbackId), eqTo(answers), eqTo(cid))(any())
      }
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      forAll(arbitrary[Origin]) { origin =>
        val postRequest = fakeRequest.withFormUrlEncodedBody(("ableToDo", "invalid value"))
        val boundForm = form.bind(Map("ableToDo" -> "invalid value"))

        val result = controller().onSubmit(origin)(postRequest)

        status(result) mustBe BAD_REQUEST
        contentAsString(result) mustBe viewAsString(form = boundForm, action = submitCall(origin))
      }
    }
  }
}
