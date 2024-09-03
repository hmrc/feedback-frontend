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

package forms.mappings

import base.BaseSpec
import play.api.data.validation.{Invalid, Valid}

class ConstraintsSpec extends BaseSpec with Constraints {

  val MAXIMUM_NUMBER_OF_CHARACTERS = 10

  "maxLength" must {

    "return Valid for a string shorter than the allowed length" in {
      val result = maxLength(MAXIMUM_NUMBER_OF_CHARACTERS, "error.length")("a" * (MAXIMUM_NUMBER_OF_CHARACTERS - 1))
      result mustEqual Valid
    }

    "return Valid for an empty string" in {
      val result = maxLength(MAXIMUM_NUMBER_OF_CHARACTERS, "error.length")("")
      result mustEqual Valid
    }

    "return Valid for a string equal to the allowed length" in {
      val result = maxLength(MAXIMUM_NUMBER_OF_CHARACTERS, "error.length")("a" * MAXIMUM_NUMBER_OF_CHARACTERS)
      result mustEqual Valid
    }

    "return Invalid for a string longer than the allowed length" in {
      val result = maxLength(MAXIMUM_NUMBER_OF_CHARACTERS, "error.length")("a" * (MAXIMUM_NUMBER_OF_CHARACTERS + 1))
      result mustEqual Invalid("error.length", MAXIMUM_NUMBER_OF_CHARACTERS)
    }
  }
}
