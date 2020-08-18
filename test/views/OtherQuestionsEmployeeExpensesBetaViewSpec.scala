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

package views

import forms.{OtherQuestionsEmployeeExpensesBetaFormProvider, OtherQuestionsFormProvider}
import models.{HowDoYouFeelQuestion, HowEasyQuestion, OtherQuestions, OtherQuestionsEmployeeExpensesBeta}
import play.api.data.Form
import views.behaviours.{OptionsViewBehaviours, QuestionViewBehaviours, StringViewBehaviours, YesNoViewBehaviours}
import views.html.{otherQuestions, otherQuestionsEmployeeExpensesBeta}

class OtherQuestionsEmployeeExpensesBetaViewSpec
    extends YesNoViewBehaviours[OtherQuestionsEmployeeExpensesBeta]
    with StringViewBehaviours[OtherQuestionsEmployeeExpensesBeta]
    with OptionsViewBehaviours[OtherQuestionsEmployeeExpensesBeta]
    with QuestionViewBehaviours[OtherQuestionsEmployeeExpensesBeta] {

  val messageKeyPrefix = "otherQuestionsEmployeeExpensesBeta"

  val form = new OtherQuestionsEmployeeExpensesBetaFormProvider()()
  val action = controllers.routes.SessionExpiredController.onPageLoad()

  def createView = () => otherQuestionsEmployeeExpensesBeta(frontendAppConfig, form, action)(fakeRequest, messages)

  def createViewUsingForm =
    (form: Form[_]) => otherQuestionsEmployeeExpensesBeta(frontendAppConfig, form, action)(fakeRequest, messages)

  "OtherQuestions view" must {

    behave like normalPage(createView, messageKeyPrefix, "intro1", "intro3", "paragraph1", "paragraph2", "paragraph3")

    behave like yesNoPage(createViewUsingForm, "ableToDo", "otherQuestionsEmployeeExpensesBeta.ableToDo")

    behave like optionsPage(
      createViewUsingForm,
      "howEasyScore",
      HowEasyQuestion.options,
      "otherQuestionsEmployeeExpensesBeta.howEasyScore")

    behave like stringPage(createViewUsingForm, "whyGiveScore", "otherQuestionsEmployeeExpensesBeta.whyGiveScore")

    behave like optionsPage(
      createViewUsingForm,
      "howDoYouFeelScore",
      HowDoYouFeelQuestion.options,
      "otherQuestionsEmployeeExpensesBeta.howDoYouFeelScore")

    behave like stringPage(createViewUsingForm, "fullName", "otherQuestionsEmployeeExpensesBeta.fullName")

    behave like stringPage(createViewUsingForm, "email", "otherQuestionsEmployeeExpensesBeta.email")

    "contain second introductory paragraph" in {
      val expectedMessage = messages(
        "otherQuestionsEmployeeExpensesBeta.intro2",
        messages("otherQuestionsEmployeeExpensesBeta.introLinkText"))
      val doc = asDocument(createView())
      assertContainsText(doc, expectedMessage)
    }

    "contain privacy anchor tag" in {
      val expectedLink = messages("otherQuestionsEmployeeExpensesBeta.introLinkText")
      val doc = asDocument(createView())
      assertContainsLink(doc, expectedLink, frontendAppConfig.privacyPolicyUrl)
    }
  }
}
