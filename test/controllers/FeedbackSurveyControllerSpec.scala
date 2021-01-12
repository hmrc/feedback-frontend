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

import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.mockito.MockitoSugar
import play.api.test.Helpers._

class FeedbackSurveyControllerSpec extends ControllerSpecBase with MockitoSugar with BeforeAndAfterEach {

  val ptaOrigin = "PERTAX"
  val nonPtaOrigin = "ATS"

  class TestFeedbackSurveyController extends FeedbackSurveyController(mcc)
  val testFeedbackSurveyController = new TestFeedbackSurveyController

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

    "redirect to general feedback page for empty origin" in {
      val result = testFeedbackSurveyController.feedbackHomePageRedirect(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result).get must include("/feedback-survey/feedback/beta")
    }
  }
}
