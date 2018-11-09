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

package views

import play.api.data.Form
import controllers.routes
import forms.OtherQuestionsFormProvider
import views.behaviours.{StringViewBehaviours, YesNoViewBehaviours}
import models.{HowDoYouFeelQuestion, HowEasyQuestion, OtherQuestions}
import viewmodels.RadioOption
import views.html.otherQuestions

class OtherQuestionsViewSpec extends YesNoViewBehaviours[OtherQuestions]
  with StringViewBehaviours[OtherQuestions] {

  val messageKeyPrefix = "otherQuestions"

  val form = new OtherQuestionsFormProvider()()

  def createView = () => otherQuestions(frontendAppConfig, form)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[_]) => otherQuestions(frontendAppConfig, form)(fakeRequest, messages)

  "OtherQuestions view" must {

    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, "ableToDo", "otherQuestions.ableToDo", routes.OtherQuestionsController.onSubmit().url)

    behave like radioOptionsPage("howEasyScore", HowEasyQuestion.options)

    behave like stringPage(createViewUsingForm, "whyGiveScore", "otherQuestions.whyGiveScore", routes.OtherQuestionsController.onSubmit().url)

    behave like radioOptionsPage("howDoYouFeelScore", HowDoYouFeelQuestion.options)
  }

  def radioOptionsPage[A](fieldName: String, options: Seq[RadioOption]) = {
    s"contain radio buttons for the $fieldName" in {
      val doc = asDocument(createViewUsingForm(form))
      for (option <- options) {
        assertContainsRadioButton(doc, option.id, fieldName, option.value, false)
      }
    }


    for(option <- options) {
      s"rendered with a $fieldName of '${option.value}'" must {
        s"have the '${option.value}' radio button selected" in {
          val doc = asDocument(createViewUsingForm(form.bind(Map(fieldName -> s"${option.value}"))))
          assertContainsRadioButton(doc, option.id, fieldName, option.value, true)

          for(unselectedOption <- options.filterNot(o => o == option)) {
            assertContainsRadioButton(doc, unselectedOption.id, fieldName, unselectedOption.value, false)
          }
        }
      }
    }
  }
}
