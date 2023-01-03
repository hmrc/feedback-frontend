/*
 * Copyright 2023 HM Revenue & Customs
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

import forms.GiveReasonFormProvider
import models.{GiveReason, GiveReasonQuestions, Origin}
import play.api.data.Form
import views.behaviours.{OptionsViewBehaviours, ViewBehaviours}
import views.html.GiveReasonView

class GiveReasonViewSpec extends ViewBehaviours with OptionsViewBehaviours[GiveReasonQuestions] {

  val messageKeyPrefix = "giveReason"

  val form = new GiveReasonFormProvider()()
  val action = controllers.routes.GiveReasonController.onPageLoad(Origin.fromString("origin"))

  lazy val giveReasonView = inject[GiveReasonView]

  def createView = () => giveReasonView(frontendAppConfig, form, action)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[_]) => giveReasonView(frontendAppConfig, form, action)(fakeRequest, messages)

  "GiveReason view" must {
    behave like normalPage(createView, messageKeyPrefix, "govuk-fieldset__heading")
  }

  "GiveReason view" when {
    "rendered" must {
      behave like optionsPage(
        createViewUsingForm,
        "value",
        GiveReason.options(form),
        messageKeyPrefix,
        "govuk-fieldset__legend--xl")
    }
  }
}
