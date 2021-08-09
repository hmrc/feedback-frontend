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
import models.NmwCcgQuestions
import models.ccg.{CheckUnderstandingQuestion, SupportFutureQuestion, TreatedProfessionallyQuestion}
import play.api.data.FormError

class NmwCcgQuestionsFormProviderSpec
    extends OptionFieldBehaviours with BooleanFieldBehaviours with StringFieldBehaviours {

  val form = new NmwCcgQuestionsFormProvider()()

  ".treatedProfessionally" must {

    val fieldName = "treatedProfessionally"
    val invalidError = "treatedProfessionally.error"

    behave like optionsField[NmwCcgQuestions, TreatedProfessionallyQuestion](
      form,
      fieldName,
      TreatedProfessionallyQuestion.values,
      FormError(fieldName, invalidError),
      _.treatedProfessionally
    )
  }

  ".checkUnderstanding" must {

    val fieldName = "checkUnderstanding"
    val invalidError = "checkUnderstandingQuestion.error"

    behave like optionsField[NmwCcgQuestions, CheckUnderstandingQuestion](
      form,
      fieldName,
      CheckUnderstandingQuestion.values,
      FormError(fieldName, invalidError),
      _.checkUnderstanding
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

  ".supportFutureNmw" must {

    val fieldName = "supportFutureNmw"
    val invalidError = "supportFutureQuestion.error"

    behave like optionsField[NmwCcgQuestions, SupportFutureQuestion](
      form,
      fieldName,
      SupportFutureQuestion.values,
      FormError(fieldName, invalidError),
      _.supportFutureNmw
    )
  }

}
