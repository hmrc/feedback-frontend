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

import forms.behaviours.{BooleanFieldBehaviours, OptionFieldBehaviours, StringFieldBehaviours}
import models.{BTAQuestions, HowDoYouFeelQuestion, HowEasyQuestion, MainServiceQuestion, TrustsQuestions, TryingToDoQuestion}
import play.api.data.FormError

class TrustsQuestionsFormProviderSpec
    extends OptionFieldBehaviours with BooleanFieldBehaviours with StringFieldBehaviours {

  def form = new TrustsQuestionsFormProvider()()

  ".isAgent" must {

    val fieldName = "isAgent"
    val invalidError = "error.boolean"

    behave like booleanField[TrustsQuestions](
      form,
      fieldName,
      invalidError = FormError(fieldName, invalidError),
      _.isAgent
    )
  }

  ".tryingToDo" must {

    val fieldName = "tryingToDo"
    val invalidError = "error.invalid"

    behave like optionsField[TrustsQuestions, TryingToDoQuestion](
      form,
      fieldName,
      TryingToDoQuestion.values,
      FormError(fieldName, invalidError),
      _.tryingToDo
    )
  }

  ".tryingToDoOther" must {

    val fieldName = "tryingToDoOther"
    val invalidError = "generic.max-characters"
    val maxLength = 100

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength,
      FormError(fieldName, invalidError, List(maxLength))
    )
  }

  ".ableToDo" must {

    val fieldName = "ableToDo"
    val invalidError = "error.boolean"

    behave like booleanField[TrustsQuestions](
      form,
      fieldName,
      invalidError = FormError(fieldName, invalidError),
      _.ableToDo
    )
  }

  ".whyNotAbleToDo" must {

    val fieldName = "whyNotAbleToDo"
    val invalidError = "whyNotAbleToDo.error.maxlength"
    val maxLength = 1000

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength,
      FormError(fieldName, invalidError, List(maxLength))
    )
  }

  ".howEasyScore" must {

    val fieldName = "howEasyScore"
    val invalidError = "error.invalid"

    behave like optionsField[TrustsQuestions, HowEasyQuestion](
      form,
      fieldName,
      HowEasyQuestion.values,
      FormError(fieldName, invalidError),
      _.howEasyScore
    )
  }

  ".whyGiveScore" must {

    val fieldName = "whyGiveScore"
    val invalidError = "whyGiveScore.error.maxlength"
    val maxLength = 1000

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength,
      FormError(fieldName, invalidError, List(maxLength))
    )
  }

  ".howDoYouFeelScore" must {

    val fieldName = "howDoYouFeelScore"
    val invalidError = "error.invalid"

    behave like optionsField[TrustsQuestions, HowDoYouFeelQuestion](
      form,
      fieldName,
      HowDoYouFeelQuestion.values,
      FormError(fieldName, invalidError),
      _.howDoYouFeelScore
    )
  }
}
