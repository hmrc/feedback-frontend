/*
 * Copyright 2021 HM Revenue & Customs
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

package forms

import forms.mappings.Mappings
import javax.inject.Inject
import models._
import models.ccg._
import play.api.data.Form
import play.api.data.Forms._

class OtherQuestionsFormProvider @Inject() extends Mappings {

  private val maxFieldSizeWhyGiveScore = 1000

  def apply(): Form[OtherQuestions] =
    Form(
      mapping(
        "ableToDo"     -> optional(boolean()),
        "howEasyScore" -> optional(enumerable[HowEasyQuestion]()),
        "whyGiveScore" ->
          optional(text("whyGiveScore.error.required")
            .verifying(maxLength(maxFieldSizeWhyGiveScore, "whyGiveScore.error.maxlength"))),
        "howDoYouFeelScore" -> optional(enumerable[HowDoYouFeelQuestion]())
      )(OtherQuestions.apply)(OtherQuestions.unapply))
}

class OtherQuestionsEmployeeExpensesBetaFormProvider @Inject() extends Mappings {

  private val maxStringFieldSize = 1000

  def apply(): Form[OtherQuestionsEmployeeExpensesBeta] =
    Form(
      mapping(
        "ableToDo"     -> optional(boolean()),
        "howEasyScore" -> optional(enumerable[HowEasyQuestion]()),
        "whyGiveScore" ->
          optional(text("whyGiveScore.error.required")
            .verifying(maxLength(maxStringFieldSize, "whyGiveScore.error.maxlength"))),
        "howDoYouFeelScore" -> optional(enumerable[HowDoYouFeelQuestion]()),
        "fullName"          -> optional(text().verifying(maxLength(maxStringFieldSize, "fullName.error.maxlength"))),
        "email"             -> optional(text().verifying(maxLength(maxStringFieldSize, "email.error.maxlength")))
      )(OtherQuestionsEmployeeExpensesBeta.apply)(OtherQuestionsEmployeeExpensesBeta.unapply))
}

class PTAQuestionsFormProvider @Inject() extends Mappings {

  private val maxFieldSizeAbleToDo = 200
  private val maxFieldSizeWhyGiveScore = 1000

  def apply(): Form[PTAQuestions] =
    Form(
      mapping(
        "neededToDo" ->
          optional(text("neededToDo.error.required")
            .verifying(maxLength(maxFieldSizeAbleToDo, "neededToDo.error.maxlength"))),
        "ableToDo"     -> optional(boolean()),
        "howEasyScore" -> optional(enumerable[HowEasyQuestion]()),
        "whyGiveScore" ->
          optional(text("whyGiveScore.error.required")
            .verifying(maxLength(maxFieldSizeWhyGiveScore, "whyGiveScore.error.maxlength"))),
        "howDoYouFeelScore" -> optional(enumerable[HowDoYouFeelQuestion]())
      )(PTAQuestions.apply)(PTAQuestions.unapply))
}

class BTAQuestionsFormProvider @Inject() extends Mappings {

  private val maxFieldSizeMainServiceOther = 100
  private val maxFieldSizeWhyGiveScore = 1000

  def apply(): Form[BTAQuestions] =
    Form(
      mapping(
        "mainService" -> optional(enumerable[MainServiceQuestion]()),
        "mainServiceOther" ->
          optional(text("mainServiceOther.error.required")
            .verifying(maxLength(maxFieldSizeMainServiceOther, "generic.max-characters"))),
        "ableToDo"     -> optional(boolean()),
        "howEasyScore" -> optional(enumerable[HowEasyQuestion]()),
        "whyGiveScore" ->
          optional(text("whyGiveScore.error.required")
            .verifying(maxLength(maxFieldSizeWhyGiveScore, "whyGiveScore.error.maxlength"))),
        "howDoYouFeelScore" -> optional(enumerable[HowDoYouFeelQuestion]())
      )(BTAQuestions.apply)(BTAQuestions.unapply))
}

class PensionQuestionsFormProvider @Inject() extends Mappings {

  private val maxFieldSizeWhyGiveScore = 1000

  def apply(): Form[PensionQuestions] =
    Form(
      mapping(
        "ableToDo"     -> optional(boolean()),
        "howEasyScore" -> optional(enumerable[HowEasyQuestion]()),
        "whyGiveScore" ->
          optional(text("whyGiveScore.error.required")
            .verifying(maxLength(maxFieldSizeWhyGiveScore, "whyGiveScore.error.maxlength"))),
        "howDoYouFeelScore" -> optional(enumerable[HowDoYouFeelQuestion]()),
        "likelyToDo"        -> optional(enumerable[LikelyToDoQuestion]())
      )(PensionQuestions.apply)(PensionQuestions.unapply))
}

class CCGQuestionsFormProvider @Inject() extends Mappings {

  private val maxFieldSizeWhyGiveAnswer = 1000

  def apply(): Form[CCGQuestions] =
    Form(
      mapping(
        "complianceCheckUnderstanding" -> optional(enumerable[ComplianceCheckUnderstandingQuestion]()),
        "treatedProfessionally"        -> optional(enumerable[TreatedProfessionallyQuestion]()),
        "whyGiveAnswer" ->
          optional(text("whyGiveAnswer.error.required")
            .verifying(maxLength(maxFieldSizeWhyGiveAnswer, "whyGiveAnswer.error.maxlength"))),
        "supportFutureTax" -> optional(enumerable[SupportFutureTaxQuestion]())
      )(CCGQuestions.apply)(CCGQuestions.unapply))
}

class NmwCcgQuestionsFormProvider extends Mappings {
  def apply(): Form[NmwQuestions] =
    Form(
      mapping(
        "treatedProfessionally" -> optional(enumerable[TreatedProfessionallyQuestion]()),
      )(NmwQuestions.apply)(NmwQuestions.unapply)
    )
}
