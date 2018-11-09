/*
 * Copyright 2018 HM Revenue & Customs
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

import play.api.libs.json._
import viewmodels.RadioOption

sealed trait HowEasyQuestion

object HowEasyQuestion {

  case object VeryEasy      extends WithName("VeryEasy") with HowEasyQuestion
  case object Easy          extends WithName("Easy") with HowEasyQuestion
  case object Moderate      extends WithName("Moderate") with HowEasyQuestion
  case object Difficult     extends WithName("Difficult") with HowEasyQuestion
  case object VeryDifficult extends WithName("VeryDifficult") with HowEasyQuestion

  val values: Set[HowEasyQuestion] =
    Set(VeryEasy, Easy, Moderate, Difficult, VeryDifficult)

  val options: Seq[RadioOption] = values.map {
    value =>
      RadioOption("howEasyQuestion", value.toString)
  }.toSeq

  implicit val enumerable: Enumerable[HowEasyQuestion] =
    Enumerable(values.toSeq.map(v => v.toString -> v): _*)

  implicit object HowEasyQuestionWrites extends Writes[HowEasyQuestion] {
    def writes(howEasyQuestion: HowEasyQuestion) = Json.toJson(howEasyQuestion.toString)
  }

  implicit object HowEasyQuestionReads extends Reads[HowEasyQuestion] {
    override def reads(json: JsValue): JsResult[HowEasyQuestion] = json match {
      case JsString(VeryEasy.toString)      => JsSuccess(VeryEasy)
      case JsString(Easy.toString)          => JsSuccess(Easy)
      case JsString(Moderate.toString)      => JsSuccess(Moderate)
      case JsString(Difficult.toString)     => JsSuccess(Difficult)
      case JsString(VeryDifficult.toString) => JsSuccess(VeryDifficult)
      case _                                => JsError("Unknown howEasyQuestion")
    }
  }
}