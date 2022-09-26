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

package navigation

import base.SpecBase
import controllers.routes
import models._
import org.scalatestplus.mockito.MockitoSugar
import pages._

class NavigatorSpec extends SpecBase with MockitoSugar {

  val navigator = new Navigator
  val origin = Origin.fromString("/foo")

  "Navigator" when {
    "GenericQuestionsPageWithOrigin" should {

      "return ThankYou page with origin" in {

        navigator.nextPage(GenericQuestionsPage)(origin) mustBe routes.ThankYouController.onPageLoadWithOrigin(origin)
      }
    }

    "GenericQuestionsPage" should {

      "return ThankYou page without origin" in {

        navigator.nextPage(GenericQuestionsPage)(()) mustBe routes.ThankYouController.onPageLoad()
      }

      "return PensionsQuestions page when given" in {
        navigator.nextPage(PensionQuestionsPage)() mustBe routes.ThankYouController.onPageLoadPension
      }
    }
  }
}
