/*
 * Copyright 2024 HM Revenue & Customs
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
import generators.ModelGenerators
import models.{CCGQuestions, Cid, FeedbackId, Origin}
import navigation.FakeNavigator
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{reset, times, verify}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.data.Form
import play.api.mvc.Call
import play.api.test.Helpers._
import services.AuditService
import views.html.CcgQuestionsView

class CCGQuestionsControllerSpec extends SpecBase with ScalaCheckPropertyChecks with ModelGenerators with MockitoSugar {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new CCGQuestionsFormProvider()
  val form: Form[CCGQuestions] = formProvider()
  lazy val mockAuditService: AuditService = mock[AuditService]
  lazy val ccgQuestionsView: CcgQuestionsView = inject[CcgQuestionsView]

  def submitCall(origin: Origin): Call = routes.CCGQuestionsController.onSubmit(origin)

  def controller() =
    new CCGQuestionsController(
      frontendAppConfig,
      new FakeNavigator(onwardRoute),
      formProvider,
      mockAuditService,
      mcc,
      ccgQuestionsView)

  def viewAsString(form: Form[_] = form, action: Call): String =
    ccgQuestionsView(frontendAppConfig, form, action)(fakeRequest, messages).toString

  "CCGQuestions Controller" must {

    "return OK and the correct view for a GET" in {
      forAll (Gen.alphaStr) { str =>
        val origin = Origin.fromString(str)
        val result = controller().onPageLoad(origin)(fakeRequest)

        status(result) mustBe OK
        contentAsString(result) mustBe viewAsString(action = submitCall(origin))
      }
    }

    "redirect to the next page when valid data is submitted" in {
      forAll (Gen.alphaStr) { str =>
        val origin = Origin.fromString(str)
        val result = controller().onSubmit(origin)(fakeRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(onwardRoute.url)
      }
    }

    "audit response on success" in {
      forAll(Gen.alphaStr, arbitrary[FeedbackId], arbitrary[CCGQuestions], arbitrary[Cid]) {
        (originStr, feedbackId, answers, cid) =>
          reset(mockAuditService)
          val origin = Origin.fromString(originStr)
          val values = Map(
            "complianceCheckUnderstanding" -> answers.complianceCheckUnderstanding.map(_.toString),
            "treatedProfessionally"        -> answers.treatedProfessionally.map(_.toString),
            "whyGiveAnswer"                -> answers.whyGiveAnswer,
            "supportFutureTax"             -> answers.supportFutureTaxQuestion.map(_.toString)
          ).map(value => (value._1, value._2.getOrElse(""))).toSeq

          val request = fakeRequest
            .withMethod("POST")
            .withFormUrlEncodedBody(values: _*)
            .withSession(("feedbackId", feedbackId.value))
            .withHeaders("referer" -> s"/feedback/ccg/cgg?cid=${cid.value}")

          val result = controller().onSubmit(origin)(request)
          status(result) mustBe SEE_OTHER

          verify(mockAuditService, times(1))
            .ccgAudit(eqTo(origin), eqTo(feedbackId), eqTo(answers), eqTo(cid))(any())
      }
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      forAll (Gen.alphaStr) { str =>
        val origin = Origin.fromString(str)
        val invalidValue = "*" * 1001
        val postRequest = fakeRequest
          .withMethod("POST")
          .withFormUrlEncodedBody(("whyGiveAnswer", invalidValue))
        val boundForm = form.bind(Map("whyGiveAnswer" -> invalidValue))

        val result = controller().onSubmit(origin)(postRequest)

        status(result) mustBe BAD_REQUEST
        contentAsString(result) mustBe viewAsString(form = boundForm, action = submitCall(origin))
      }
    }
  }
}
