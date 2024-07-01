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

import forms.TrustsQuestionsFormProvider
import models.{AbleToDo, HowDoYouFeelQuestion, HowEasyQuestion, MainServiceQuestion, TrustsQuestions, TryingToDoQuestion, YesNo}
import play.api.data.Form
import play.api.mvc.Call
import play.twirl.api.HtmlFormat
import views.behaviours.{OptionsViewBehaviours, StringViewBehaviours, YesNoViewBehaviours}
import views.html.TrustsQuestionsView

class TrustsQuestionsViewSpec
  extends YesNoViewBehaviours[TrustsQuestions] with StringViewBehaviours[TrustsQuestions] with OptionsViewBehaviours[TrustsQuestions] {

  val messageKeyPrefix = "trustsQuestions"

  val form = new TrustsQuestionsFormProvider()()
  val action: Call = controllers.routes.SessionExpiredController.onPageLoad

  lazy val trustsQuestionsView: TrustsQuestionsView = inject[TrustsQuestionsView]

  def createView: () => HtmlFormat.Appendable = () => trustsQuestionsView(frontendAppConfig, form, action)(fakeRequest, messages)

  def createViewUsingForm: Form[_] => HtmlFormat.Appendable =
    (form: Form[_]) => trustsQuestionsView(frontendAppConfig, form, action)(fakeRequest, messages)

  "TrustsQuestions view" must {
    behave like normalPage(createView, messageKeyPrefix, "govuk-heading-xl", "intro1", "intro3")

    behave like optionsPage(createViewUsingForm, "isAgent", YesNo.options(form), "trustsQuestions.isAgent")

    behave like optionsPage(
      createViewUsingForm,
      "tryingToDo",
      TryingToDoQuestion.options(form),
      "trustsQuestions.tryingToDo")

    behave like stringPage(createViewUsingForm, "tryingToDoOther", "trustsQuestions.tryingToDo")

    behave like optionsPage(createViewUsingForm, "ableToDo", AbleToDo.options(form), "trustsQuestions.ableToDo")

    behave like stringPage(createViewUsingForm, "whyNotAbleToDo", "trustsQuestions.whyNotAbleToDo")

    behave like optionsPage(
      createViewUsingForm,
      "howEasyScore",
      HowEasyQuestion.options(form),
      "trustsQuestions.howEasyScore")

    behave like stringPage(createViewUsingForm, "whyGiveScore", "trustsQuestions.whyGiveScore")

    behave like optionsPage(
      createViewUsingForm,
      "howDoYouFeelScore",
      HowDoYouFeelQuestion.options(form),
      "trustsQuestions.howDoYouFeelScore")

    "contain second introductory paragraph" in {
      val expectedMessage = messages("trustsQuestions.intro2", messages("trustsQuestions.introLinkText"))
      val doc = asDocument(createView())
      assertContainsText(doc, expectedMessage)
    }

    "contain privacy anchor tag" in {
      val expectedLink = messages("trustsQuestions.introLinkText")
      val doc = asDocument(createView())
      assertContainsLink(doc, expectedLink, frontendAppConfig.privacyPolicyUrl)
    }
  }
}
