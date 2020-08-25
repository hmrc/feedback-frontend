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

import forms.EOTHOQuestionsFormProvider
import models._
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.{CheckboxViewBehaviours, OptionsViewBehaviours, StringViewBehaviours, YesNoViewBehaviours}
import views.html.eothoQuestions

class EOTHOQuestionsViewSpec
    extends YesNoViewBehaviours[EOTHOQuestions] with StringViewBehaviours[EOTHOQuestions]
    with OptionsViewBehaviours[EOTHOQuestions] with CheckboxViewBehaviours[EOTHOQuestions] {

  val messageKeyPrefix = "eothoQuestions"

  val form = new EOTHOQuestionsFormProvider()()
  val action = controllers.routes.SessionExpiredController.onPageLoad()

  def createView = () => eothoQuestions(frontendAppConfig, form, action)(fakeRequest, messages)

  def createViewUsingForm =
    (form: Form[_]) => eothoQuestions(frontendAppConfig, form, action)(fakeRequest, messages)

  "EOTHOQuestions view" must {

    behave like normalPage(createView, messageKeyPrefix, "intro1", "intro3")

    behave like optionsPage(
      createViewUsingForm,
      "numberOfEstablishments",
      NumberOfEstablishmentsQuestion.options,
      "eothoQuestions.numberOfEstablishments")

    behave like optionsPage(
      createViewUsingForm,
      "comparedToMonTueWed",
      ComparedToMonTueWedQuestion.options,
      "eothoQuestions.comparedToMonTueWed")

    behave like optionsPage(
      createViewUsingForm,
      "comparedToThurFriSatSun",
      ComparedToThurFriSatSunQuestion.options,
      "eothoQuestions.comparedToThurFriSatSun")

    behave like checkboxPage(form, createViewUsingForm, messageKeyPrefix, WhichRegionQuestion.options, "whichRegions")

    behave like optionsPage(
      createViewUsingForm,
      "comparedBusinessTurnover",
      ComparedBusinessTurnoverQuestion.options,
      "eothoQuestions.comparedBusinessTurnover")

    behave like optionsPage(
      createViewUsingForm,
      "affectedJobs",
      AffectedJobsQuestion.options,
      "eothoQuestions.affectedJobs")

    behave like optionsPage(
      createViewUsingForm,
      "furloughEmployees",
      FurloughEmployeesQuestion.options,
      "eothoQuestions.furloughEmployees")

    behave like optionsPage(
      createViewUsingForm,
      "businessFuturePlans",
      BusinessFuturePlansQuestion.options,
      "eothoQuestions.businessFuturePlans")

    behave like optionsPage(
      createViewUsingForm,
      "offerDiscounts",
      OfferDiscountsQuestion.options,
      "eothoQuestions.offerDiscounts")

    // TODO reinstate this test
//    behave like checkboxPage(checkboxForm, applyView, messageKeyPrefix, WhichRegionQuestion.options)

    "contain privacy anchor tag" in {
      val expectedLink = messages("eothoQuestions.introLinkText")
      val doc = asDocument(createView())
      assertContainsLink(doc, expectedLink, frontendAppConfig.privacyPolicyUrl)
    }
  }
}
