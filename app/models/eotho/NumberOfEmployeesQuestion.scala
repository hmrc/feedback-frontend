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

sealed trait NumberOfEmployeesQuestion

object NumberOfEmployeesQuestion {

  case object NoEmployees extends WithName("NoEmployees") with NumberOfEmployeesQuestion
  case object OneToTenEmployees extends WithName("OneToTenEmployees") with NumberOfEmployeesQuestion
  case object ElevenToHundredEmployees extends WithName("ElevenToHundredEmployees") with NumberOfEmployeesQuestion
  case object HundredToFiveHundredEmployees
      extends WithName("HundredToFiveHundredEmployees") with NumberOfEmployeesQuestion
  case object FiveHundredOrMoreEmployees extends WithName("FiveHundredOrMoreEmployees") with NumberOfEmployeesQuestion

  val values: Seq[NumberOfEmployeesQuestion] =
    List(
      NoEmployees,
      OneToTenEmployees,
      ElevenToHundredEmployees,
      HundredToFiveHundredEmployees,
      FiveHundredOrMoreEmployees
    )

  val options: Seq[RadioOption] = values.map { value =>
    RadioOption("numberOfEmployeesQuestion", value.toString)
  }

  implicit val enumerable: Enumerable[NumberOfEmployeesQuestion] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object NumberOfEmployeesQuestion extends Writes[NumberOfEmployeesQuestion] {
    def writes(numberOfEmployeesQuesiton: NumberOfEmployeesQuestion) =
      Json.toJson(numberOfEmployeesQuesiton.toString)
  }

  implicit object NumberOfEmployeesQuestionReads extends Reads[NumberOfEmployeesQuestion] {
    override def reads(json: JsValue): JsResult[NumberOfEmployeesQuestion] = json match {
      case JsString(OneToTenEmployees.toString) =>
        JsSuccess(OneToTenEmployees)
      case JsString(ElevenToHundredEmployees.toString) =>
        JsSuccess(ElevenToHundredEmployees)
      case JsString(HundredToFiveHundredEmployees.toString) =>
        JsSuccess(HundredToFiveHundredEmployees)
      case JsString(FiveHundredOrMoreEmployees.toString) =>
        JsSuccess(FiveHundredOrMoreEmployees)
      case _ => JsError("Unknown NumberOfEmployeesQuestion")
    }
  }
}
