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

import forms.behaviours.{BooleanFieldBehaviours, OptionFieldBehaviours, StringFieldBehaviours}
import models.{AbleToDo, DidWithNinoQuestion, HowDoYouFeelQuestion, HowEasyQuestion, NinoQuestions, YesNo}
import play.api.data.FormError

class NinoQuestionsFormProviderSpec
  extends OptionFieldBehaviours with BooleanFieldBehaviours with StringFieldBehaviours {

  def form = new NinoQuestionsFormProvider()()


  ".ableToDo" must {

    val fieldName = "ableToDo"
    val invalidError = "ableToDo.error"

    behave like optionsField[NinoQuestions, AbleToDo](
      form,
      fieldName,
      AbleToDo.values,
      FormError(fieldName, invalidError),
      _.ableToDo
    )
  }

  ".howEasyScore" must {

    val fieldName = "howEasyScore"
    val invalidError = "howEasyScore.error"

    behave like optionsField[NinoQuestions, HowEasyQuestion](
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
    val invalidError = "howDoYouFeelScore.error"

    behave like optionsField[NinoQuestions, HowDoYouFeelQuestion](
      form,
      fieldName,
      HowDoYouFeelQuestion.values,
      FormError(fieldName, invalidError),
      _.howDoYouFeelScore
    )
  }

  ".logInToSeeNino" must {

    val fieldName = "logInToSeeNino"
    val invalidError = "logInToSeeNino.error"

    behave like optionsField[NinoQuestions, YesNo](
      form,
      fieldName,
      YesNo.values,
      FormError(fieldName, invalidError),
      _.logInToSeeNino
    )
  }

  ".didWithNino" must {

    val fieldName = "didWithNino"
    val invalidError = "didWithNino.error"

    behave like optionsField[NinoQuestions, DidWithNinoQuestion](
      form,
      fieldName,
      DidWithNinoQuestion.values,
      FormError(fieldName, invalidError),
      _.didWithNino
    )
  }

  ".whyGiveAnswer" must {

    val fieldName = "whyGiveAnswer"
    val invalidError = "whyGiveAnswer.error.maxlength"
    val maxLength = 1000

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength,
      FormError(fieldName, invalidError, List(maxLength))
    )
  }
}