/*
 * Copyright 2018 HM Revenue & Customs
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

import play.api.data.Form
import navigation.FakeNavigator
import connectors.FakeDataCacheConnector
import controllers.actions._
import play.api.test.Helpers._
import forms.PTAQuestionsFormProvider
import generators.ModelGenerators
import models.PTAQuestions
import org.scalacheck.Arbitrary._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.prop.PropertyChecks
import play.api.mvc.Call
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import views.html.pTAQuestions

import org.mockito.Matchers._
import org.mockito.Matchers.{eq => eqTo}
import org.mockito.Mockito._

class PTAQuestionsControllerSpec extends ControllerSpecBase with PropertyChecks with ModelGenerators with MockitoSugar {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new PTAQuestionsFormProvider()
  val form = formProvider()
  lazy val mockAuditConnector = mock[AuditConnector]
  def submitCall(origin: String) = routes.PTAQuestionsController.onSubmit(origin)

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new PTAQuestionsController(
      frontendAppConfig,
      messagesApi,
      new FakeNavigator(onwardRoute),
      formProvider,
      mockAuditConnector)

  def viewAsString(form: Form[_] = form, action: Call) =
    pTAQuestions(frontendAppConfig, form, action)(fakeRequest, messages).toString

  "PTAQuestions Controller" must {

    "return OK and the correct view for a GET" in {
      forAll(arbitrary[String]) { origin =>
        val result = controller().onPageLoad(origin)(fakeRequest)

        status(result) mustBe OK
        contentAsString(result) mustBe viewAsString(action = submitCall(origin))
      }
    }

    "redirect to the next page when valid data is submitted" in {
      forAll(arbitrary[String]) { origin =>
        val result = controller().onSubmit(origin)(fakeRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(onwardRoute.url)
      }
    }

    "audit response on success" in {
      forAll(arbitrary[String], arbitrary[PTAQuestions]) {
        (origin, answers) =>
          reset(mockAuditConnector)

          whenever(answers.whyGiveScore.exists(_ != "")) {
            val values = Map(
              "ableToDo" -> answers.ableToDo.map(_.toString),
              "howEasyScore" -> answers.howEasyScore.map(_.toString),
              "whyGiveScore" -> answers.whyGiveScore,
              "howDoYouFeelScore" -> answers.howDoYouFeelScore.map(_.toString))

            val request = fakeRequest.withFormUrlEncodedBody(values.mapValues(_.getOrElse("")).toList: _*)
            controller().onSubmit(origin)(request)

            val expectedValues = values.mapValues(_.getOrElse("-")) + ("origin" -> origin)

            verify(mockAuditConnector, times(1))
              .sendExplicitAudit(eqTo("feedback"), eqTo(expectedValues))(any(), any())
          }
      }
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      forAll(arbitrary[String]) { origin =>
        val postRequest = fakeRequest.withFormUrlEncodedBody(("ableToDo", "invalid value"))
        val boundForm = form.bind(Map("ableToDo" -> "invalid value"))

        val result = controller().onSubmit(origin)(postRequest)

        status(result) mustBe BAD_REQUEST
        contentAsString(result) mustBe viewAsString(form = boundForm, action = submitCall(origin))
      }
    }
  }
}
