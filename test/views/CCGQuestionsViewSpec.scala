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

import forms.CCGQuestionsFormProvider
import models.CCGQuestions
import models.ccg.{ComplianceCheckUnderstandingQuestion, SupportFutureTaxQuestion, TreatedProfessionallyQuestion}
import play.api.data.Form
import views.behaviours._
import views.html.ccgQuestions

class CCGQuestionsViewSpec extends StringViewBehaviours[CCGQuestions] with OptionsViewBehaviours[CCGQuestions] {

  val messageKeyPrefix = "ccgQuestions"

  val form = new CCGQuestionsFormProvider()()
  val action = controllers.routes.SessionExpiredController.onPageLoad()

  def createView = () => ccgQuestions(frontendAppConfig, form, action)(fakeRequest, messages)

  def createViewUsingForm =
    (form: Form[_]) => ccgQuestions(frontendAppConfig, form, action)(fakeRequest, messages)

  "CCGQuestions view" must {
    behave like normalPage(createView, messageKeyPrefix, "intro1", "intro3")

    behave like optionsPage(
      createViewUsingForm,
      "treatedProfessionally",
      TreatedProfessionallyQuestion.options,
      "ccgQuestions.treatedProfessionally")

    behave like optionsPage(
      createViewUsingForm,
      "complianceCheckUnderstanding",
      ComplianceCheckUnderstandingQuestion.options,
      "ccgQuestions.complianceCheckUnderstanding")

    behave like stringPage(createViewUsingForm, "whyGiveAnswer", "ccgQuestions.whyGiveAnswer")

    behave like optionsPage(
      createViewUsingForm,
      "supportFutureTax",
      SupportFutureTaxQuestion.options,
      "ccgQuestions.supportFutureTax")

    "contain second introductory paragraph" in {
      val expectedMessage = messages("ccgQuestions.intro2", messages("ccgQuestions.introLinkText"))
      val doc = asDocument(createView())
      assertContainsText(doc, expectedMessage)
    }

    "contain privacy anchor tag" in {
      val expectedLink = messages("ccgQuestions.introLinkText")
      val doc = asDocument(createView())
      assertContainsLink(doc, expectedLink, frontendAppConfig.privacyPolicyUrl)
    }
  }
}
