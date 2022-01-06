/*
 * Copyright 2022 HM Revenue & Customs
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

import play.api.data.{Form, FormError}

trait BooleanFieldBehaviours extends FieldBehaviours {

  def booleanField[A](
    form: Form[A],
    fieldName: String,
    invalidError: FormError,
    fieldValue: A => Option[Boolean]
  ): Unit = {

    "bind true" in {
      val result = form.bind(Map(fieldName -> "true"))
      fieldValue(result.value.value) mustBe Some(true)
    }

    "bind false" in {
      val result = form.bind(Map(fieldName -> "false"))
      fieldValue(result.value.value) mustBe Some(false)
    }

    "not bind non-booleans" in {

      forAll(nonBooleans -> "nonBoolean") { nonBoolean =>
        val result = form.bind(Map(fieldName -> nonBoolean)).apply(fieldName)
        result.errors mustBe Seq(invalidError)
      }
    }
  }
}
