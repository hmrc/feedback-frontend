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
import forms.NmwCcgQuestionsFormProvider
import generators.ModelGenerators
import models.{FeedbackId, NmwCcgQuestions, Origin}
import navigation.FakeNavigator
import org.mockito.Matchers.{any, eq => eqTo}
import org.mockito.Mockito.{reset, times, verify}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
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
  val submitCall = routes.NmwCcgQuestionsController.onSubmit()
  val viewAsString = nmwCcgQuestionsView(frontendAppConfig, form, submitCall)(fakeRequest, messages).toString
  lazy val mockAuditService = mock[AuditService]
  val origin = Origin.fromString("nmw")

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
      val result = controller.onPageLoad()(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString
    }
  }

  "redirect to the next page when valid data is submitted" in {
    val result = controller.onSubmit()(fakeRequest)

    status(result) mustBe SEE_OTHER
    redirectLocation(result) mustBe Some(onwardRoute.url)

  }

  "audit response on success" in {
    forAll(arbitrary[FeedbackId], arbitrary[NmwCcgQuestions]) { (feedbackId, answers) =>
      reset(mockAuditService)

      val values = Map(
        "treatedProfessionally" -> answers.treatedProfessionally.map(_.toString),
        "checkUnderstanding"    -> answers.checkUnderstanding.map(_.toString),
        "whyGiveAnswer"         -> answers.whyGiveAnswer,
        "supportFutureNmw"      -> answers.supportFutureNmw.map(_.toString)
      )

      val request = fakeRequest.withFormUrlEncodedBody(values.mapValues(_.getOrElse("")).toList: _*)
      val result = controller.onSubmit()(request.withSession(("feedbackId", feedbackId.value)))
      status(result) mustBe SEE_OTHER

      verify(mockAuditService, times(1))
        .nmwCcgAudit(eqTo(origin), eqTo(feedbackId), eqTo(answers))(any())
    }
  }

  "return a Bad Request and errors when invalid data is submitted" in {
    val invalidValue = "*" * 1001
    val postRequest = fakeRequest.withFormUrlEncodedBody(("whyGiveAnswer", invalidValue))
    val boundForm = form.bind(Map("whyGiveAnswer" -> invalidValue))
    val viewAsString = nmwCcgQuestionsView(frontendAppConfig, boundForm, submitCall)(fakeRequest, messages).toString
    val result = controller.onSubmit()(postRequest)

    status(result) mustBe BAD_REQUEST
    contentAsString(result) mustBe viewAsString

  }
}
