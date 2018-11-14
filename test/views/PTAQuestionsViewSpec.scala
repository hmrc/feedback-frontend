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
import forms.PTAQuestionsFormProvider
import views.behaviours.{OptionsViewBehaviours, StringViewBehaviours, YesNoViewBehaviours}
import models.{HowDoYouFeelQuestion, HowEasyQuestion, PTAQuestions}
import views.html.ptaQuestions

class PTAQuestionsViewSpec extends YesNoViewBehaviours[PTAQuestions]
  with StringViewBehaviours[PTAQuestions]
  with OptionsViewBehaviours[PTAQuestions] {

  val messageKeyPrefix = "ptaQuestions"

  val form = new PTAQuestionsFormProvider()()
  val action = controllers.routes.IndexController.onPageLoad()

  def createView = () => ptaQuestions(frontendAppConfig, form, action)(fakeRequest, messages)

  def createViewUsingForm =
    (form: Form[_]) => ptaQuestions(frontendAppConfig, form, action)(fakeRequest, messages)

  "PTAQuestions view" must {
    behave like normalPage(createView, messageKeyPrefix, "intro1", "intro3")

    behave like stringPage(
      createViewUsingForm,
      "neededToDo",
      "ptaQuestions.ptaService")

    behave like yesNoPage(
      createViewUsingForm,
      "ableToDo",
      "ptaQuestions.ableToDo")

    behave like optionsPage(
      createViewUsingForm,
      "howEasyScore",
      HowEasyQuestion.options,
      "ptaQuestions.howEasyScore")

    behave like stringPage(
      createViewUsingForm,
      "whyGiveScore",
      "ptaQuestions.whyGiveScore")

    behave like optionsPage(
      createViewUsingForm,
      "howDoYouFeelScore",
      HowDoYouFeelQuestion.options,
      "ptaQuestions.howDoYouFeelScore")

    "contain second introductory paragraph" in {
      val expectedMessage = messages("ptaQuestions.intro2", messages("ptaQuestions.introLinkText"))
      val doc = asDocument(createView())
      assertContainsText(doc, expectedMessage)
    }

    "contain privacy anchor tag" in {
      val expectedLink = messages("ptaQuestions.introLinkText")
      val doc = asDocument(createView())
      assertContainsLink(doc, expectedLink, frontendAppConfig.privacyPolicyUrl)
    }
  }
}
