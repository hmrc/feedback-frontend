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

import forms.ComplaintFeedbackQuestionsFormProvider
import models.{ComplaintFeedbackQuestions, HowDoYouFeelQuestion, HowEasyQuestion, Origin, YesNo}
import play.api.data.Form
import play.api.mvc.Call
import play.twirl.api.HtmlFormat
import views.behaviours.{OptionsViewBehaviours, StringViewBehaviours, YesNoViewBehaviours}
import views.html.ComplaintFeedbackQuestionsView

class ComplaintFeedbackQuestionsViewSpec
  extends YesNoViewBehaviours[ComplaintFeedbackQuestions] with StringViewBehaviours[ComplaintFeedbackQuestions]
  with OptionsViewBehaviours[ComplaintFeedbackQuestions] {

  val form = new ComplaintFeedbackQuestionsFormProvider()()
  val messageKeyPrefix = "complaintFeedbackQuestions"

  val action: Call = controllers.routes.ComplaintFeedbackQuestionsController.onPageLoad(Origin.fromString("origin"))

  lazy val complaintFeedbackQuestionsView: ComplaintFeedbackQuestionsView = inject[ComplaintFeedbackQuestionsView]

  def createView: () => HtmlFormat.Appendable = () => complaintFeedbackQuestionsView(frontendAppConfig, form, action)(fakeRequest, messages)

  def createViewUsingForm: Form[?] => HtmlFormat.Appendable =
    (form: Form[?]) => complaintFeedbackQuestionsView(frontendAppConfig, form, action)(fakeRequest, messages)


  "ComplaintFeedbackQuestions view" must {

    behave like normalPage(
      createView,
      messageKeyPrefix,
      "govuk-heading-xl",
      "intro1",
      "intro3")

    behave like optionsPage(
      createViewUsingForm,
      "complaintHandledFairly",
      YesNo.options(form),
      "complaintFeedbackQuestions.complaintHandledFairly")

    behave like optionsPage(
      createViewUsingForm,
      "howEasyScore",
      HowEasyQuestion.options(form),
      "complaintFeedbackQuestions.howEasyScore")

    behave like stringPage(
      createViewUsingForm,
      "whyGiveScore",
      "complaintFeedbackQuestions.whyGiveScore")

    behave like optionsPage(
      createViewUsingForm,
      "howDoYouFeelScore",
      HowDoYouFeelQuestion.options(form),
      "complaintFeedbackQuestions.howDoYouFeelScore")

    "contain second introductory paragraph" in {
      val expectedMessage = messages("complaintFeedbackQuestions.intro2", messages("complaintFeedbackQuestions.introLinkText"))
      val doc = asDocument(createView())
      assertContainsText(doc, expectedMessage)
    }

    "contain privacy anchor tag" in {
      val expectedLink = messages("complaintFeedbackQuestions.introLinkText")
      val doc = asDocument(createView())
      assertContainsLink(doc, expectedLink, frontendAppConfig.privacyPolicyUrl)
    }
  }
}
