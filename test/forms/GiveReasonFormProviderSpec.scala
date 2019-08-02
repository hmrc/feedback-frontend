/*
 * Copyright 2019 HM Revenue & Customs
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

import forms.behaviours.OptionFieldBehaviours
import models.{GiveReason, GiveReasonQuestions}
import play.api.data.FormError

class GiveReasonFormProviderSpec extends OptionFieldBehaviours {

  val form = new GiveReasonFormProvider()()

  ".value" must {

    val fieldName = "value"

    behave like optionsField[GiveReasonQuestions, GiveReason](
      form,
      fieldName,
      validValues = GiveReason.values.toList,
      invalidError = FormError(fieldName, "error.invalid"),
      fieldValue = _.value
    )
  }
}
