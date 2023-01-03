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

package models.ccg

import base.BaseSpec
import org.scalacheck.Arbitrary.arbitrary
import play.api.libs.json.{JsError, JsString, Json}

class TreatedProfessionallyQuestionSpec extends BaseSpec {

  val gen = arbitrary[TreatedProfessionallyQuestion]

  "TreatedProfessionallyQuestion" must {

    "deserialise valid values" in {

      forAll(gen) { value =>
        JsString(value.toString).validate[TreatedProfessionallyQuestion].asOpt.value mustEqual value
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!TreatedProfessionallyQuestion.values.map(_.toString).contains(_))

      forAll(gen) { invalidValue =>
        JsString(invalidValue).validate[TreatedProfessionallyQuestion] mustEqual JsError("error.invalid")
      }
    }

    "serialise" in {

      forAll(gen) { value =>
        Json.toJson(value) mustEqual JsString(value.toString)
      }
    }
  }

}
