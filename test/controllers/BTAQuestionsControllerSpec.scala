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
import forms.BTAQuestionsFormProvider
import generators.ModelGenerators
import models.{BTAQuestions, FeedbackId, Origin}
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
import views.html.btaQuestions

class BTAQuestionsControllerSpec
    extends ControllerSpecBase with ScalaCheckPropertyChecks with ModelGenerators with MockitoSugar with ScalaFutures {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new BTAQuestionsFormProvider()
  val form = formProvider()
  lazy val mockAuditService = mock[AuditService]

  def submitCall(origin: Origin) = routes.BTAQuestionsController.onSubmit(origin)

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new BTAQuestionsController(
      frontendAppConfig,
      new FakeNavigator(onwardRoute),
      formProvider,
      mockAuditService,
      mcc
    )

  def viewAsString(form: Form[_] = form, action: Call) =
    btaQuestions(frontendAppConfig, form, action)(fakeRequest, messages).toString

  "BTAQuestions Controller" must {

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
      forAll(arbitrary[Origin], arbitrary[FeedbackId], arbitrary[BTAQuestions]) { (origin, feedbackId, answers) =>
        reset(mockAuditService)

        val values = Map(
          "mainService"       -> answers.mainService.map(_.toString),
          "mainServiceOther"  -> answers.mainServiceOther.map(_.toString),
          "ableToDo"          -> answers.ableToDo.map(_.toString),
          "howEasyScore"      -> answers.howEasyScore.map(_.toString),
          "whyGiveScore"      -> answers.whyGiveScore,
          "howDoYouFeelScore" -> answers.howDoYouFeelScore.map(_.toString)
        )

        val request = fakeRequest.withFormUrlEncodedBody(values.mapValues(_.getOrElse("")).toList: _*)
        controller().onSubmit(origin)(request.withSession(("feedbackId", feedbackId.value))).futureValue

        verify(mockAuditService, times(1))
          .btaAudit(eqTo(origin), eqTo(feedbackId), eqTo(answers))(any())
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
