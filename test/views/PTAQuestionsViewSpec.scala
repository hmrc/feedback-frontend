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

import play.api.data.Form
import forms.PTAQuestionsFormProvider
import views.behaviours.{OptionsViewBehaviours, StringViewBehaviours, YesNoViewBehaviours}
import models.{AbleToDo, HowDoYouFeelQuestion, HowEasyQuestion, Origin, PTAQuestions}
import play.api.mvc.Call
import play.twirl.api.HtmlFormat
import views.html.PtaQuestionsView

class PTAQuestionsViewSpec
  extends YesNoViewBehaviours[PTAQuestions] with StringViewBehaviours[PTAQuestions] with OptionsViewBehaviours[PTAQuestions] {

  val messageKeyPrefix = "ptaQuestions"

  val form = new PTAQuestionsFormProvider()()
  val action: Call = controllers.routes.PTAQuestionsController.onPageLoad(Origin.fromString("origin"))

  lazy val ptaQuestionsView: PtaQuestionsView = inject[PtaQuestionsView]

  def createView: () => HtmlFormat.Appendable = () => ptaQuestionsView(frontendAppConfig, form, action)(fakeRequest, messages)

  def createViewUsingForm: Form[?] => HtmlFormat.Appendable =
    (form: Form[?]) => ptaQuestionsView(frontendAppConfig, form, action)(fakeRequest, messages)

  "PTAQuestions view" must {
    behave like normalPage(createView, messageKeyPrefix, "govuk-heading-xl", "intro1", "intro3")

    behave like stringPage(
      createViewUsingForm,
      "neededToDo",
      "ptaQuestions.neededToDo",
      Some("ptaQuestions.neededToDo.heading.hintText"))

    behave like optionsPage(createViewUsingForm, "ableToDo", AbleToDo.options(form), "ptaQuestions.ableToDo")

    behave like optionsPage(
      createViewUsingForm,
      "howEasyScore",
      HowEasyQuestion.options(form),
      "ptaQuestions.howEasyScore")

    behave like stringPage(createViewUsingForm, "whyGiveScore", "ptaQuestions.whyGiveScore")

    behave like optionsPage(
      createViewUsingForm,
      "howDoYouFeelScore",
      HowDoYouFeelQuestion.options(form),
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
