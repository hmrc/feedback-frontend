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

import forms.NmwCcgQuestionsFormProvider
import models.NmwCcgQuestions
import models.ccg.{CheckUnderstandingQuestion, SupportFutureQuestion, TreatedProfessionallyQuestion}
import play.api.data.Form
import play.api.mvc.Call
import play.twirl.api.HtmlFormat
import views.behaviours.{OptionsViewBehaviours, StringViewBehaviours}
import views.html.NmwCcgQuestionsView

class NmwCcgQuestionsViewSpec
    extends StringViewBehaviours[NmwCcgQuestions]
    with OptionsViewBehaviours[NmwCcgQuestions] {

  val messageKeyPrefix = "nmwCcgQuestions"

  val form         = new NmwCcgQuestionsFormProvider()()
  val action: Call = controllers.routes.SessionExpiredController.onPageLoad

  lazy val nmwCcgQuestionsView: NmwCcgQuestionsView = inject[NmwCcgQuestionsView]

  def createView: () => HtmlFormat.Appendable = () =>
    nmwCcgQuestionsView(frontendAppConfig, form, action)(fakeRequest, messages)

  def createViewUsingForm: Form[?] => HtmlFormat.Appendable =
    (form: Form[?]) => nmwCcgQuestionsView(frontendAppConfig, form, action)(fakeRequest, messages)

  "NmwCcgQuestionsView" must {

    behave like normalPage(createView, messageKeyPrefix, "govuk-heading-xl", "intro1", "intro3")

    behave like optionsPage(
      createViewUsingForm,
      "treatedProfessionally",
      TreatedProfessionallyQuestion.options(form),
      "nmwCcgQuestions.treatedProfessionally"
    )

    behave like optionsPage(
      createViewUsingForm,
      "checkUnderstanding",
      CheckUnderstandingQuestion.options(form),
      "nmwCcgQuestions.CheckUnderstanding"
    )

    behave like stringPage(createViewUsingForm, "whyGiveAnswer", "nmwCcgQuestions.whyGiveAnswer")

    behave like optionsPage(
      createViewUsingForm,
      "supportFutureNmw",
      SupportFutureQuestion.options(form),
      "nmwCcgQuestions.supportFutureNmw"
    )

  }

  "contain second introductory paragraph" in {
    val expectedMessage = messages("nmwCcgQuestions.intro2", messages("nmwCcgQuestions.introLinkText"))
    val doc             = asDocument(createView())
    assertContainsText(doc, expectedMessage)
  }

  "contain diclaimer hint text" in {
    val expectedMessage = messages("nmwCcgQuestions.whyGiveAnswer.hint")
    val doc             = asDocument(createView())
    assertContainsText(doc, expectedMessage)
  }

  "contain privacy anchor tag" in {
    val expectedLink = messages("nmwCcgQuestions.introLinkText")
    val doc          = asDocument(createView())
    assertContainsLink(doc, expectedLink, frontendAppConfig.privacyPolicyUrl)
  }
}
