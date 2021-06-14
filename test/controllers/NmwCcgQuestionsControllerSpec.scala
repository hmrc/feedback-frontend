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

import forms.NmwCcgQuestionsFormProvider
import generators.ModelGenerators
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.data.Form
import play.api.mvc.Call
import play.api.test.Helpers._
import services.AuditService
import views.html.NmwCcgQuestionsView

class NmwCcgQuestionsControllerSpec
    extends ControllerSpecBase with ScalaCheckPropertyChecks with ModelGenerators with MockitoSugar {

  val formProvider = new NmwCcgQuestionsFormProvider()
  val form = formProvider()
  lazy val nmwCcgQuestionsView = inject[NmwCcgQuestionsView]
  val submitCall = routes.NmwCcgQuestionsController.onSubmit()
  val viewAsString = nmwCcgQuestionsView(frontendAppConfig, form, submitCall)(fakeRequest, messages).toString

  val controller = new NmwCcgQuestionsController(
    frontendAppConfig,
    mcc,
    nmwCcgQuestionsView,
    formProvider
  )

  "CCGQuestions Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller.onPageLoad()(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString
    }
  }

}
