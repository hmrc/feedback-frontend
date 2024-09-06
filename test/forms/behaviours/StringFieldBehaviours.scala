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
import play.api.data.{Form, FormError}

import scala.util.Random

trait StringFieldBehaviours extends BaseSpec {

  def fieldWithMaxLength(form: Form[_], fieldName: String, maxLength: Int, lengthError: FormError): Unit = {
    s"not bind strings longer than $maxLength characters" in {

      for (_ <- 1 to 10) {
        val numberOfCharacters = Random.between(maxLength + 2, maxLength + 11)
        val veryLongString = Random.alphanumeric.take(numberOfCharacters).mkString
        val field = form.bind(Map(fieldName -> veryLongString)).apply(fieldName)
        field.errors mustEqual Seq(lengthError)
      }

    }

    "trim spaces before validation" in {

      for (_ <- 1 to 10) {
        val maximumLengthString = Random.alphanumeric.take(maxLength).mkString
        val maximumLengthStringWithSpaces = s"     $maximumLengthString     "
        val field = form.bind(Map(fieldName -> maximumLengthStringWithSpaces)).apply(fieldName)
        field.hasErrors mustBe false
      }

    }
  }
}
