/*
 * Copyright 2018 HM Revenue & Customs
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

import javax.inject.Inject
import forms.mappings.Mappings
import models._
import play.api.data.Form
import play.api.data.Forms._

class OtherQuestionsFormProvider @Inject() extends Mappings {

  private val maxFieldSize = 1000

  def apply(): Form[OtherQuestions] =
    Form(mapping(
      "ableToDo" -> optional(boolean()),
      "howEasyScore" -> optional(enumerable[HowEasyQuestion]()),
      "whyGiveScore" ->
        optional(text("whyGiveScore.error.required")
          .verifying(maxLength(maxFieldSize, "whyGiveScore.error.maxLength"))),
      "howDoYouFeelScore" -> optional(enumerable[HowDoYouFeelQuestion]())
    )(OtherQuestions.apply)(OtherQuestions.unapply))
}

class BTAQuestionsFormProvider @Inject() extends Mappings {

  private val maxFieldSize = 1000

  def apply(): Form[BTAQuestions] =
    Form(mapping(
      "service" -> optional(enumerable[BTAServiceQuestion]()),
      "ableToDo" -> optional(boolean()),
      "howEasyScore" -> optional(enumerable[HowEasyQuestion]()),
      "whyGiveScore" ->
        optional(text("whyGiveScore.error.required")
          .verifying(maxLength(maxFieldSize, "whyGiveScore.error.maxLength"))),
      "howDoYouFeelScore" -> optional(enumerable[HowDoYouFeelQuestion]())
    )(BTAQuestions.apply)(BTAQuestions.unapply))
}

class PTAQuestionsFormProvider @Inject() extends Mappings {

  private val maxFieldSize = 1000

  def apply(): Form[PTAQuestions] =
    Form(mapping(
      "neededToDo" ->
        optional(text("neededToDo.error.required")
          .verifying(maxLength(maxFieldSize, "neededToDo.error.maxLength"))),
      "ableToDo" -> optional(boolean()),
      "howEasyScore" -> optional(enumerable[HowEasyQuestion]()),
      "whyGiveScore" ->
        optional(text("whyGiveScore.error.required")
          .verifying(maxLength(maxFieldSize, "whyGiveScore.error.maxLength"))),
      "howDoYouFeelScore" -> optional(enumerable[HowDoYouFeelQuestion]())
    )(PTAQuestions.apply)(PTAQuestions.unapply))
}

