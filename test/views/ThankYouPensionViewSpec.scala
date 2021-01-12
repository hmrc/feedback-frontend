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

package views

import views.behaviours.ViewBehaviours
import views.html.thankYouPension

class ThankYouPensionViewSpec extends ViewBehaviours {

  val messageKeyPrefix = "thankYou.pension"

  def createView = () => thankYouPension(frontendAppConfig)(fakeRequest, messages)

  "ThankYou view" must {
    behave like normalPage(createView, messageKeyPrefix)

    "contain pension intro paragraph" in {
      val expectedMessage = messages("thankYou.pension.intro")
      val doc = asDocument(createView())
      assertContainsText(doc, expectedMessage)
    }

    "contain pension subHeading" in {
      val expectedMessage = messages("thankYou.pension.subHeading")
      val doc = asDocument(createView())
      assertContainsText(doc, expectedMessage)
    }

    "contain pension subContent" in {
      val expectedMessage = messages("thankYou.pension.subContent")
      val doc = asDocument(createView())
      assertContainsText(doc, expectedMessage)
    }

    "contain sign in link" in {
      val expectedLink = messages("thankYou.pension.signInLink")
      val doc = asDocument(createView())
      assertContainsLink(doc, expectedLink, frontendAppConfig.pensionSignInUrl.getOrElse(""))
    }

    "contain retirement link" in {
      val expectedLink = messages("thankYou.pension.retirementLink")
      val doc = asDocument(createView())
      assertContainsLink(doc, expectedLink, frontendAppConfig.pensionRetirementUrl.getOrElse(""))
    }

    "contain pension wise link in sidebar" in {
      val expectedLink = messages("thankYou.pension.sideBar.linkOne")
      val doc = asDocument(createView())
      assertContainsLink(doc, expectedLink, frontendAppConfig.pensionSideBarOneUrl.getOrElse(""))
    }

    "contain planning your retirement link in sidebar" in {
      val expectedLink = messages("thankYou.pension.sideBar.linkTwo")
      val doc = asDocument(createView())
      assertContainsLink(doc, expectedLink, frontendAppConfig.pensionSideBarTwoUrl.getOrElse(""))
    }

    "contain defer link in sidebar" in {
      val expectedLink = messages("thankYou.pension.sideBar.linkThree")
      val doc = asDocument(createView())
      assertContainsLink(doc, expectedLink, frontendAppConfig.pensionSideBarThreeUrl.getOrElse(""))
    }

    "contain contact link in sidebar" in {
      val expectedLink = messages("thankYou.pension.sideBar.linkFour")
      val doc = asDocument(createView())
      assertContainsLink(doc, expectedLink, frontendAppConfig.pensionSideBarFourUrl.getOrElse(""))
    }

    "contain more link in sidebar" in {
      val expectedLink = messages("thankYou.pension.sideBar.linkFive")
      val doc = asDocument(createView())
      assertContainsLink(doc, expectedLink, frontendAppConfig.pensionSideBarFiveUrl.getOrElse(""))
    }
  }
}
