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

package models.ccg

import base.BaseSpec
import base.CommonSpecValues.{invalidAnswers, supportFutureQuestionNumberOfOptions}
import play.api.libs.json.{JsError, JsString, Json}

import scala.util.Random

class SupportFutureQuestionSpec extends BaseSpec {

  "SupportFutureQuestion" must {

    "deserialise valid values" in {

      for (_ <- 1 to 10) {
        val answer = SupportFutureQuestion.values(Random.nextInt(supportFutureQuestionNumberOfOptions))
        JsString(answer.toString).validate[SupportFutureQuestion].asOpt.value mustEqual answer
      }

    }

    "fail to deserialise invalid values" in {

      for (answer <- invalidAnswers)
        JsString(answer).validate[SupportFutureQuestion] mustEqual JsError("error.invalid")

    }

    "serialise" in {

      for (_ <- 1 to 10) {
        val answer = SupportFutureQuestion.values(Random.nextInt(supportFutureQuestionNumberOfOptions))
        Json.toJson(answer) mustEqual JsString(answer.toString)
      }

    }
  }

}
