/*
 * Copyright 2023 HM Revenue & Customs
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
import play.api.data.{Form, Forms}
import play.api.data.Forms._

class OtherQuestionsFormProvider @Inject() extends Mappings {

  private val maxFieldSizeWhyGiveScore = 1000

  def apply(): Form[OtherQuestions] =
    Form(
      mapping(
        "ableToDo"     -> optional(enumerable[AbleToDo](invalidKey = "ableToDo.error")),
        "howEasyScore" -> optional(enumerable[HowEasyQuestion](invalidKey = "howEasyScore.error")),
        "whyGiveScore" ->
          optional(text("whyGiveScore.error.required")
            .verifying(maxLength(maxFieldSizeWhyGiveScore, "whyGiveScore.error.maxlength"))),
        "howDoYouFeelScore" -> optional(enumerable[HowDoYouFeelQuestion](invalidKey = "howDoYouFeelScore.error"))
      )(OtherQuestions.apply)(OtherQuestions.unapply))
}

class NinoQuestionsFormProvider @Inject() extends Mappings {

  private val maxFieldSizeWhyGiveScore = 1000
  private val maxFieldSizeWhyGiveAnswer = 1000

  def apply(): Form[NinoQuestions] =
    Form(
      mapping(
        "ableToDo"     -> optional(enumerable[AbleToDo](invalidKey = "ableToDo.error")),
        "howEasyScore" -> optional(enumerable[HowEasyQuestion](invalidKey = "howEasyScore.error")),
        "whyGiveScore" ->
          optional(text("whyGiveScore.error.required")
            .verifying(maxLength(maxFieldSizeWhyGiveScore, "whyGiveScore.error.maxlength"))),
        "howDoYouFeelScore" -> optional(enumerable[HowDoYouFeelQuestion](invalidKey = "howDoYouFeelScore.error")),
        "logInToSeeNino" -> optional(enumerable[YesNo](invalidKey = "logInToSeeNino.error")),
        "didWithNino" -> optional(seq(exclusiveSeqElem[DidWithNinoQuestion](invalidKey = "didWithNino.error", exclusiveOptionName = "noneOfAbove"))),
        "whyGiveAnswer" ->
          optional(text("whyGiveAnswer.error.required")
            .verifying(maxLength(maxFieldSizeWhyGiveAnswer, "whyGiveAnswer.error.maxlength")))
      )(NinoQuestions.apply)(NinoQuestions.unapply))
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
        "ableToDo"     -> optional(enumerable[AbleToDo](invalidKey = "ableToDo.error")),
        "howEasyScore" -> optional(enumerable[HowEasyQuestion](invalidKey = "howEasyScore.error")),
        "whyGiveScore" ->
          optional(text("whyGiveScore.error.required")
            .verifying(maxLength(maxFieldSizeWhyGiveScore, "whyGiveScore.error.maxlength"))),
        "howDoYouFeelScore" -> optional(enumerable[HowDoYouFeelQuestion](invalidKey = "howDoYouFeelScore.error"))
      )(PTAQuestions.apply)(PTAQuestions.unapply))
}

class BTAQuestionsFormProvider @Inject() extends Mappings {

  private val maxFieldSizeMainServiceOther = 100
  private val maxFieldSizeWhyGiveScore = 1000

  def apply(): Form[BTAQuestions] =
    Form(
      mapping(
        "mainService" -> optional(enumerable[MainServiceQuestion](invalidKey = "mainService.error")),
        "mainServiceOther" ->
          optional(text("mainServiceOther.error.required")
            .verifying(maxLength(maxFieldSizeMainServiceOther, "otherService.max-characters"))),
        "ableToDo"     -> optional(enumerable[AbleToDo](invalidKey = "ableToDo.error")),
        "howEasyScore" -> optional(enumerable[HowEasyQuestion](invalidKey = "howEasyScore.error")),
        "whyGiveScore" ->
          optional(text("whyGiveScore.error.required")
            .verifying(maxLength(maxFieldSizeWhyGiveScore, "whyGiveScore.error.maxlength"))),
        "howDoYouFeelScore" -> optional(enumerable[HowDoYouFeelQuestion](invalidKey = "howDoYouFeelScore.error"))
      )(BTAQuestions.apply)(BTAQuestions.unapply))
}

class TrustsQuestionsFormProvider @Inject() extends Mappings {

  private val maxFieldSizeTryingToDoOther = 100
  private val maxFieldSizeWhyNotAbleToDo = 1000
  private val maxFieldSizeWhyGiveScore = 1000

  def apply(): Form[TrustsQuestions] =
    Form(
      mapping(
        "isAgent"    -> optional(enumerable[YesNo](invalidKey = "isAgent.error")),
        "tryingToDo" -> optional(enumerable[TryingToDoQuestion](invalidKey = "tryingToDo.error")),
        "tryingToDoOther" ->
          optional(text("tryingToDoOther.error.required")
            .verifying(maxLength(maxFieldSizeTryingToDoOther, "generic.max-characters"))),
        "ableToDo" -> optional(enumerable[AbleToDo](invalidKey = "ableToDo.error")),
        "whyNotAbleToDo" -> optional(text("whyNotAbleToDo.error.required")
          .verifying(maxLength(maxFieldSizeWhyNotAbleToDo, "whyNotAbleToDo.error.maxlength"))),
        "howEasyScore" -> optional(enumerable[HowEasyQuestion](invalidKey = "howEasyScore.error")),
        "whyGiveScore" ->
          optional(text("whyGiveScore.error.required")
            .verifying(maxLength(maxFieldSizeWhyGiveScore, "whyGiveScore.error.maxlength"))),
        "howDoYouFeelScore" -> optional(enumerable[HowDoYouFeelQuestion](invalidKey = "howDoYouFeelScore.error"))
      )(TrustsQuestions.apply)(TrustsQuestions.unapply))
}

class PensionQuestionsFormProvider @Inject() extends Mappings {

  private val maxFieldSizeWhyGiveScore = 1000

  def apply(): Form[PensionQuestions] =
    Form(
      mapping(
        "ableToDo"          -> optional(enumerable[AbleToDo](invalidKey = "ableToDo.error")),
        "howEasyScore"      -> optional(enumerable[HowEasyQuestion](invalidKey = "howEasyScore.error")),
        "whyGiveAnswer"     -> optional(Forms.text.verifying(maxLength(maxFieldSizeWhyGiveScore, "whyGiveAnswer.error.maxlength"))),
        "howDoYouFeelScore" -> optional(enumerable[HowDoYouFeelQuestion](invalidKey = "howDoYouFeelScore.error")),
        "likelyToDo"        -> optional(enumerable[LikelyToDoQuestion](invalidKey = "likelyToDo.error"))
      )(PensionQuestions.apply)(PensionQuestions.unapply))
}

class CCGQuestionsFormProvider @Inject() extends Mappings {

  private val maxFieldSizeWhyGiveAnswer = 1000

  def apply(): Form[CCGQuestions] =
    Form(
      mapping(
        "complianceCheckUnderstanding" -> optional(
          enumerable[CheckUnderstandingQuestion](invalidKey = "complianceCheckUnderstanding.error")),
        "treatedProfessionally" -> optional(
          enumerable[TreatedProfessionallyQuestion](invalidKey = "complianceTreatedProfessionally.error")),
        "whyGiveAnswer" ->
          optional(text("whyGiveAnswer.error.required")
            .verifying(maxLength(maxFieldSizeWhyGiveAnswer, "whyGiveAnswer.error.maxlength"))),
        "supportFutureTax" -> optional(enumerable[SupportFutureQuestion](invalidKey = "supportFutureTax.error"))
      )(CCGQuestions.apply)(CCGQuestions.unapply))
}

class NmwCcgQuestionsFormProvider @Inject() extends Mappings {

  private val maxFieldSizeWhyGiveAnswer = 1000

  def apply(): Form[NmwCcgQuestions] =
    Form(
      mapping(
        "treatedProfessionally" -> optional(
          enumerable[TreatedProfessionallyQuestion](invalidKey = "treatedProfessionally.error")),
        "checkUnderstanding" -> optional(
          enumerable[CheckUnderstandingQuestion](invalidKey = "checkUnderstandingQuestion.error")),
        "whyGiveAnswer" ->
          optional(
            text("whyGiveAnswer.error.required")
              .verifying(maxLength(maxFieldSizeWhyGiveAnswer, "whyGiveAnswer.error.maxlength"))),
        "supportFutureNmw" -> optional(enumerable[SupportFutureQuestion](invalidKey = "supportFutureQuestion.error"))
      )(NmwCcgQuestions.apply)(NmwCcgQuestions.unapply)
    )
}

class ComplaintFeedbackQuestionsFormProvider @Inject() extends Mappings {

  private val maxFieldSizeWhyGiveScore = 1000

  def apply(): Form[ComplaintFeedbackQuestions] =
    Form(
      mapping(
        "complaintHandledFairly"    -> optional(enumerable[YesNo](invalidKey = "complaintHandledFairly.error")),
        "howEasyScore" -> optional(enumerable[HowEasyQuestion](invalidKey = "howEasyScore.error")),
        "whyGiveScore" -> optional(text("whyGiveScore.error.required")
          .verifying(maxLength(maxFieldSizeWhyGiveScore, "whyGiveScore.error.maxlength"))),
        "howDoYouFeelScore" -> optional(enumerable[HowDoYouFeelQuestion](invalidKey = "howDoYouFeelScore.error"))
      )(ComplaintFeedbackQuestions.apply)(ComplaintFeedbackQuestions.unapply))
}