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

package views

import forms.PensionQuestionsFormProvider
import models.{AbleToDo, HowDoYouFeelQuestion, HowEasyQuestion, LikelyToDoQuestion, Origin, PensionQuestions}
import play.api.data.Form
import play.api.mvc.Call
import play.twirl.api.HtmlFormat
import views.behaviours.{OptionsViewBehaviours, StringViewBehaviours, YesNoViewBehaviours}
import views.html.PensionQuestionsView

class PensionQuestionsViewSpec
  extends YesNoViewBehaviours[PensionQuestions] with StringViewBehaviours[PensionQuestions] with OptionsViewBehaviours[PensionQuestions] {

  val messageKeyPrefix = "pensionQuestions"

  val form = new PensionQuestionsFormProvider()()
  val action: Call = controllers.routes.PensionQuestionsController.onPageLoad(Origin.fromString("origin"))

  lazy val pensionQuestionsView: PensionQuestionsView = inject[PensionQuestionsView]

  def createView: () => HtmlFormat.Appendable = () => pensionQuestionsView(frontendAppConfig, form, action)(fakeRequest, messages)

  def createViewUsingForm: Form[?] => HtmlFormat.Appendable =
    (form: Form[?]) => pensionQuestionsView(frontendAppConfig, form, action)(fakeRequest, messages)

  "PensionQuestions view" must {

    behave like normalPage(createView, messageKeyPrefix, "govuk-heading-xl", "intro1", "intro3")

    behave like optionsPage(createViewUsingForm, "ableToDo", AbleToDo.options(form), "pensionQuestions.ableToDo")

    behave like optionsPage(
      createViewUsingForm,
      "howEasyScore",
      HowEasyQuestion.options(form),
      "pensionQuestions.howEasyScore")

    behave like stringPage(createViewUsingForm, "whyGiveScore", "pensionQuestions.whyGiveScore")

    behave like optionsPage(
      createViewUsingForm,
      "howDoYouFeelScore",
      HowDoYouFeelQuestion.options(form),
      "pensionQuestions.howDoYouFeelScore")

    behave like optionsPage(
      createViewUsingForm,
      "likelyToDo",
      LikelyToDoQuestion.options(form),
      "pensionQuestions.likelyToDo")

    "contain diclaimer hint text" in {
      val expectedMessage = messages("pensionQuestions.whyGiveScore.hint")
      val doc = asDocument(createView())
      assertContainsText(doc, expectedMessage)
    }

    "contain privacy anchor tag" in {
      val expectedLink = messages("pensionQuestions.introLinkText")
      val doc = asDocument(createView())
      assertContainsLink(doc, expectedLink, frontendAppConfig.privacyPolicyUrl)
    }
  }
}
