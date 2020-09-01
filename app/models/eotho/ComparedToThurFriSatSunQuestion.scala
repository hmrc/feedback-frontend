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

package models.eotho

import models.{Enumerable, WithName}
import play.api.libs.json._
import viewmodels.RadioOption

sealed trait ComparedToThurFriSatSunQuestion

object ComparedToThurFriSatSunQuestion {

  case object DecreasedByMoreThan50percent
      extends WithName("DecreasedByMoreThan50percent") with ComparedToThurFriSatSunQuestion
  case object DecreasedBetween20And50percent
      extends WithName("DecreasedBetween20And50percent") with ComparedToThurFriSatSunQuestion
  case object DecreasedByLess20percent extends WithName("DecreasedByLess20percent") with ComparedToThurFriSatSunQuestion
  case object StayedAboutTheSame extends WithName("StayedAboutTheSame") with ComparedToThurFriSatSunQuestion
  case object IncreasedByLess20percent extends WithName("IncreasedByLess20percent") with ComparedToThurFriSatSunQuestion
  case object IncreasedBetween20And50percent
      extends WithName("IncreasedBetween20And50percent") with ComparedToThurFriSatSunQuestion
  case object IncreasedByMore50percent extends WithName("IncreasedByMore50percent") with ComparedToThurFriSatSunQuestion

  val values: Seq[ComparedToThurFriSatSunQuestion] =
    List(
      DecreasedByMoreThan50percent,
      DecreasedBetween20And50percent,
      DecreasedByLess20percent,
      StayedAboutTheSame,
      IncreasedByLess20percent,
      IncreasedBetween20And50percent,
      IncreasedByMore50percent
    )

  val options: Seq[RadioOption] = values.map { value =>
    RadioOption("comparedToThurFriSatSunQuestion", value.toString)
  }

  implicit val enumerable: Enumerable[ComparedToThurFriSatSunQuestion] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object ComparedToThurFriSatSunQuestion extends Writes[ComparedToThurFriSatSunQuestion] {
    def writes(comparedToThurFriSatSunQuestion: ComparedToThurFriSatSunQuestion) =
      Json.toJson(comparedToThurFriSatSunQuestion.toString)
  }

  implicit object ComparedToThurFriSatSunQuestionReads extends Reads[ComparedToThurFriSatSunQuestion] {
    override def reads(json: JsValue): JsResult[ComparedToThurFriSatSunQuestion] = json match {
      case JsString(DecreasedByMoreThan50percent.toString)   => JsSuccess(DecreasedByMoreThan50percent)
      case JsString(DecreasedBetween20And50percent.toString) => JsSuccess(DecreasedBetween20And50percent)
      case JsString(DecreasedByLess20percent.toString)       => JsSuccess(DecreasedByLess20percent)
      case JsString(StayedAboutTheSame.toString)             => JsSuccess(StayedAboutTheSame)
      case JsString(IncreasedByLess20percent.toString)       => JsSuccess(IncreasedByLess20percent)
      case JsString(IncreasedBetween20And50percent.toString) => JsSuccess(IncreasedBetween20And50percent)
      case JsString(IncreasedByMore50percent.toString)       => JsSuccess(IncreasedByMore50percent)
      case _                                                 => JsError("Unknown ComparedToThurFriSatSunQuestion")
    }
  }
}
