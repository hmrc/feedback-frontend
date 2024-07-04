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
import models.Origin
import play.api.test.Helpers._
import views.html.{ThankYou, ThankYouPensionView}

class ThankYouControllerSpec extends SpecBase {

  lazy val thankYouView: ThankYou = inject[ThankYou]
  lazy val thankYouPensionView: ThankYouPensionView = inject[ThankYouPensionView]

  def controller() =
    new ThankYouController(frontendAppConfig, mcc, thankYouView, thankYouPensionView)

  def viewAsString(): String = thankYouView(frontendAppConfig)(fakeRequest, messages).toString
  val origin: Origin = Origin.fromString("/foo")

  "ThankYou Controller" must {

    "return OK and the correct view for a GET with Origin" in {
      val result = controller().onPageLoadWithOrigin(origin)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "return OK and the correct view for a GET without Origin" in {
      val result = controller().onPageLoad()(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "return OK and the correct view for a GET pension page" in {
      def viewAsString() = thankYouPensionView(frontendAppConfig)(fakeRequest, messages).toString
      val result = controller().onPageLoadPension()(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }
  }
}
