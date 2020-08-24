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

sealed trait ComparedToMonTueWedQuestion

object ComparedToMonTueWedQuestion {

  case object DecreasedByMore50pc extends WithName("DecreasedByMoreThan50percent") with ComparedToMonTueWedQuestion
  case object DecreasedByLess50pc extends WithName("DecreasedByLessThan50percent") with ComparedToMonTueWedQuestion
  case object StayAboutTheSame extends WithName("StayedAboutTheSame") with ComparedToMonTueWedQuestion
  case object IncreasedByLess50pc extends WithName("IncreasedByLessThan50percent") with ComparedToMonTueWedQuestion
  case object IncreasedByMore50pc extends WithName("IncreasedByMoreThan50percent") with ComparedToMonTueWedQuestion

  val values: Seq[ComparedToMonTueWedQuestion] =
    List(DecreasedByMore50pc, DecreasedByLess50pc, StayAboutTheSame, IncreasedByLess50pc, IncreasedByMore50pc)

  val options: Seq[RadioOption] = values.map { value =>
    RadioOption("comparedToMonTueWedQuestion", value.toString)
  }

  implicit val enumerable: Enumerable[ComparedToMonTueWedQuestion] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object ComparedToMonTueWedQuestion extends Writes[ComparedToMonTueWedQuestion] {
    def writes(comparedToMonTueWedQuestion: ComparedToMonTueWedQuestion) =
      Json.toJson(comparedToMonTueWedQuestion.toString)
  }

  implicit object ComparedToMonTueWedQuestionReads extends Reads[ComparedToMonTueWedQuestion] {
    override def reads(json: JsValue): JsResult[ComparedToMonTueWedQuestion] = json match {
      case JsString(DecreasedByMore50pc.toString) => JsSuccess(DecreasedByMore50pc)
      case JsString(DecreasedByLess50pc.toString) => JsSuccess(DecreasedByLess50pc)
      case JsString(StayAboutTheSame.toString)    => JsSuccess(StayAboutTheSame)
      case JsString(IncreasedByLess50pc.toString) => JsSuccess(IncreasedByLess50pc)
      case JsString(IncreasedByMore50pc.toString) => JsSuccess(IncreasedByMore50pc)
      case _                                      => JsError("Unknown ComparedToMonTueWedQuestion")
    }
  }
}
