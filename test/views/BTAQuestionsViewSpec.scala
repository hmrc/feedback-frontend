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

import forms.BTAQuestionsFormProvider
import models.{BTAQuestions, BTAServiceQuestion, HowDoYouFeelQuestion, HowEasyQuestion}
import play.api.data.Form
import views.behaviours.{OptionsViewBehaviours, StringViewBehaviours, YesNoViewBehaviours}
import views.html.btaQuestions

class BTAQuestionsViewSpec extends YesNoViewBehaviours[BTAQuestions]
  with OptionsViewBehaviours[BTAQuestions]
  with StringViewBehaviours[BTAQuestions] {

  val messageKeyPrefix = "btaQuestions"
  val form = new BTAQuestionsFormProvider()()

  def createView = () => btaQuestions(frontendAppConfig, form)(fakeRequest, messages)

  def createViewUsingForm =
    (form: Form[_]) => btaQuestions(frontendAppConfig, form)(fakeRequest, messages)

  "BTAQuestions view" must {
    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)

    behave like optionsPage(
      createViewUsingForm,
      "service",
      BTAServiceQuestion.options,
      "btaQuestions.btaService")

    behave like yesNoPage(
      createViewUsingForm,
      "ableToDo",
      "btaQuestions.ableToDo")

    behave like optionsPage(
      createViewUsingForm,
      "howEasyScore",
      HowEasyQuestion.options,
      "btaQuestions.howEasyScore")

    behave like stringPage(
      createViewUsingForm,
      "whyGiveScore",
      "btaQuestions.whyGiveScore")

    behave like optionsPage(
      createViewUsingForm,
      "howDoYouFeelScore",
      HowDoYouFeelQuestion.options,
      "btaQuestions.howDoYouFeelScore")
  }
}
