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

package forms.behaviours

import base.BaseSpec
import base.CommonSpecValues.invalidFormFieldValues
import play.api.data.{Form, FormError}

class OptionFieldBehaviours extends BaseSpec {

  def optionsField[A, T](
    form: Form[A],
    fieldName: String,
    validValues: Seq[T],
    invalidError: FormError,
    fieldValue: A => Option[T]
  ): Unit = {

    "bind all valid values" in {
      for (value <- validValues) {
        val result = form.bind(Map(fieldName -> value.toString))
        fieldValue(result.value.value) mustEqual Some(value)
      }
    }

    "not bind invalid values" in {

      for (invalidValue <- invalidFormFieldValues) {
        val field = form.bind(Map(fieldName -> invalidValue)).apply(fieldName)
        field.errors mustEqual Seq(invalidError)
      }

    }
  }
}
