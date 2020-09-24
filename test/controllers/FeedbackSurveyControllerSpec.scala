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

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import models.Origin
import org.scalatestplus.mockito.MockitoSugar
import play.api.test.Helpers._
import services.{AuditService, OriginService}
import org.mockito.Mockito.{times, verify, when}
import org.mockito.Matchers.any
import org.mockito.Mockito
import org.scalatest.BeforeAndAfterEach
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest

class FeedbackSurveyControllerSpec extends ControllerSpecBase with MockitoSugar with BeforeAndAfterEach {

  val ptaOrigin = "PERTAX"
  val nonPtaOrigin = "ATS"
  implicit val as = ActorSystem()
  implicit val mat = ActorMaterializer()
  val mockAuditService = mock[AuditService]

  def testRequest(origin: String): FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest(GET, "/feedback-survey/").withSession("originService" -> s"$origin")

  class TestFeedbackSurveyController
      extends FeedbackSurveyController(mockAuditService, new OriginService(frontendAppConfig), mcc)
  val testFeedbackSurveyController = new TestFeedbackSurveyController

  override def beforeEach(): Unit = {
    Mockito.reset(mockAuditService)
    super.beforeEach()
  }

  "FeedbackSurveyController" should {

    "redirect to pta feedback page for pta origin" in {
      val result = testFeedbackSurveyController.feedbackRedirect(ptaOrigin)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result).get must include("/feedback-survey/PERTAX/personal")
    }

    "redirect to general feedback page for non-pta origin" in {
      val result = testFeedbackSurveyController.feedbackRedirect(nonPtaOrigin)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result).get must include("/feedback-survey/ATS/beta")
    }

    "redirect to pta feedback page for pta origin as non string" in {
      val result = testFeedbackSurveyController.feedbackHomePageRedirect(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result).get must include("/feedback-survey/feedback/beta")
    }

    "audit the ableToDo data and redirect to pta feedback page for pta origin" in {
      val result = testFeedbackSurveyController.ableToDoContinue(ptaOrigin)(testRequest(ptaOrigin)).run()
      status(result) mustBe SEE_OTHER
      redirectLocation(result).get must include("/feedback-survey/PERTAX/personal")
      verify(mockAuditService, times(1)).feedbackSurveyAbleToDoAudit(any(), any())(any())
    }

    "audit the ableToDo data and redirect to general feedback page for non-pta origin" in {
      val result = testFeedbackSurveyController.ableToDoContinue(nonPtaOrigin)(testRequest(nonPtaOrigin)).run()
      status(result) mustBe SEE_OTHER
      redirectLocation(result).get must include("/feedback-survey/ATS/beta")
      verify(mockAuditService, times(1)).feedbackSurveyAbleToDoAudit(any(), any())(any())
    }

    "audit the usingService data and redirect to general feedback page for non-pta origin" in {
      val result = testFeedbackSurveyController.usingServiceContinue(nonPtaOrigin)(testRequest(nonPtaOrigin)).run()
      status(result) mustBe SEE_OTHER
      redirectLocation(result).get must include("/feedback-survey/ATS/beta")
      verify(mockAuditService, times(1)).feedbackSurveyUsingServiceAudit(any(), any())(any())
    }

    "audit the aboutService data and redirect to general feedback page for non-pta origin" in {
      val result = testFeedbackSurveyController.aboutServiceContinue(nonPtaOrigin)(testRequest(nonPtaOrigin)).run()
      status(result) mustBe SEE_OTHER
      redirectLocation(result).get must include("/feedback-survey/ATS/beta")
      verify(mockAuditService, times(1)).feedbackSurveyAboutServiceAudit(any(), any())(any())
    }

    "audit the recommendService data and redirect to general feedback page for non-pta origin" in {
      val result = testFeedbackSurveyController.recommendServiceContinue(nonPtaOrigin)(testRequest(nonPtaOrigin)).run()
      status(result) mustBe SEE_OTHER
      redirectLocation(result).get must include("/feedback-survey/ATS/beta")
      verify(mockAuditService, times(1)).feedbackSurveyRecommendServiceAudit(any(), any(), any())(any())
    }

    "audit the recommendService data and redirect to custom Feedback page for childcareSupport origin" in {
      val result = testFeedbackSurveyController.recommendServiceContinue("CC")(testRequest("CC")).run()
      status(result) mustBe SEE_OTHER
      redirectLocation(result).get must include("/childcare-calculator/survey/childcareSupport")
      verify(mockAuditService, times(1)).feedbackSurveyRecommendServiceAudit(any(), any(), any())(any())
    }
  }
}
