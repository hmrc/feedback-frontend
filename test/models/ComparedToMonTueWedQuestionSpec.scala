/*
 * Copyright 2020 HM Revenue & Customs
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

package models

import generators.ModelGenerators
import models.eotho.ComparedToMonTueWedQuestion
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.{MustMatchers, OptionValues, WordSpec}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsError, JsString, Json}

class ComparedToMonTueWedQuestionSpec
    extends WordSpec with MustMatchers with ScalaCheckPropertyChecks with OptionValues with ModelGenerators {

  "ComparedToMonTueWedQuestion" must {

    "deserialise valid values" in {

      val gen = arbitrary[ComparedToMonTueWedQuestion]

      forAll(gen) { whatRegion =>
        JsString(whatRegion.toString).validate[ComparedToMonTueWedQuestion].asOpt.value mustEqual whatRegion
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!ComparedToMonTueWedQuestion.values.map(_.toString).contains(_))

      forAll(gen) { invalidValue =>
        JsString(invalidValue).validate[ComparedToMonTueWedQuestion] mustEqual JsError("error.invalid")
      }
    }

    "serialise" in {

      val gen = arbitrary[ComparedToMonTueWedQuestion]

      forAll(gen) { whatRegion =>
        Json.toJson(whatRegion) mustEqual JsString(whatRegion.toString)
      }
    }
  }
}
