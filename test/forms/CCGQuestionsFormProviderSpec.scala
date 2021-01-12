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

import forms.behaviours._
import models.CCGQuestions
import models.ccg._
import play.api.data.FormError

class CCGQuestionsFormProviderSpec
    extends OptionFieldBehaviours with BooleanFieldBehaviours with StringFieldBehaviours {
  def form = new CCGQuestionsFormProvider()()

  ".complianceCheckUnderstanding" must {

    val fieldName = "complianceCheckUnderstanding"
    val invalidError = "error.invalid"

    behave like optionsField[CCGQuestions, ComplianceCheckUnderstandingQuestion](
      form,
      fieldName,
      ComplianceCheckUnderstandingQuestion.values,
      FormError(fieldName, invalidError),
      _.complianceCheckUnderstanding
    )
  }

  ".treatedProfessionally" must {

    val fieldName = "treatedProfessionally"
    val invalidError = "error.invalid"

    behave like optionsField[CCGQuestions, TreatedProfessionallyQuestion](
      form,
      fieldName,
      TreatedProfessionallyQuestion.values,
      FormError(fieldName, invalidError),
      _.treatedProfessionally
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

  ".supportFutureTax" must {

    val fieldName = "supportFutureTax"
    val invalidError = "error.invalid"

    behave like optionsField[CCGQuestions, SupportFutureTaxQuestion](
      form,
      fieldName,
      SupportFutureTaxQuestion.values,
      FormError(fieldName, invalidError),
      _.supportFutureTaxQuestion
    )
  }

}
