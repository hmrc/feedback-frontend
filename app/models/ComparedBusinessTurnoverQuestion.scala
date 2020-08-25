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

import play.api.libs.json._
import viewmodels.RadioOption

sealed trait ComparedBusinessTurnoverQuestion

object ComparedBusinessTurnoverQuestion {

  case object DecreasedByMore50pc extends WithName("DecreasedByMoreThan50percent") with ComparedBusinessTurnoverQuestion
  case object DecreasedByLess50pc extends WithName("DecreasedByLessThan50percent") with ComparedBusinessTurnoverQuestion
  case object StayAboutTheSame extends WithName("StayedAboutTheSame") with ComparedBusinessTurnoverQuestion
  case object IncreasedByLess50pc extends WithName("IncreasedByLessThan50percent") with ComparedBusinessTurnoverQuestion
  case object IncreasedByMore50pc extends WithName("IncreasedByMoreThan50percent") with ComparedBusinessTurnoverQuestion

  val values: Seq[ComparedBusinessTurnoverQuestion] =
    List(DecreasedByMore50pc, DecreasedByLess50pc, StayAboutTheSame, IncreasedByLess50pc, IncreasedByMore50pc)

  val options: Seq[RadioOption] = values.map { value =>
    RadioOption("comparedBusinessTurnover", value.toString)
  }

  implicit val enumerable: Enumerable[ComparedBusinessTurnoverQuestion] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object ComparedBusinessTurnoverQuestion extends Writes[ComparedBusinessTurnoverQuestion] {
    def writes(comparedBusinessTurnoverQuestion: ComparedBusinessTurnoverQuestion) =
      Json.toJson(comparedBusinessTurnoverQuestion.toString)
  }

  implicit object ComparedBusinessTurnoverQuestionReads extends Reads[ComparedBusinessTurnoverQuestion] {
    override def reads(json: JsValue): JsResult[ComparedBusinessTurnoverQuestion] = json match {
      case JsString(DecreasedByMore50pc.toString) => JsSuccess(DecreasedByMore50pc)
      case JsString(DecreasedByLess50pc.toString) => JsSuccess(DecreasedByLess50pc)
      case JsString(StayAboutTheSame.toString)    => JsSuccess(StayAboutTheSame)
      case JsString(IncreasedByLess50pc.toString) => JsSuccess(IncreasedByLess50pc)
      case JsString(IncreasedByMore50pc.toString) => JsSuccess(IncreasedByMore50pc)
      case _                                      => JsError("Unknown ComparedBusinessTurnover question")
    }
  }
}
