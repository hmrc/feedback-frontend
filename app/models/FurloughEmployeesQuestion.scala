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

sealed trait FurloughEmployeesQuestion

object FurloughEmployeesQuestion {

  case object YesFurloughedMostEmployees extends WithName("YesFurloughedMostEmployees") with FurloughEmployeesQuestion
  case object YesFurloughedSomeEmployees extends WithName("YesFurloughedSomeEmployees") with FurloughEmployeesQuestion
  case object NoDidnotFurloughAnyEmployees
      extends WithName("NoDidnotFurloughAnyEmployees") with FurloughEmployeesQuestion

  val values: Seq[FurloughEmployeesQuestion] =
    List(
      YesFurloughedMostEmployees,
      YesFurloughedSomeEmployees,
      NoDidnotFurloughAnyEmployees
    )

  val options: Seq[RadioOption] = values.map { value =>
    RadioOption("furloughEmployees", value.toString)
  }

  implicit val enumerable: Enumerable[FurloughEmployeesQuestion] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object FurloughEmployeesQuestion extends Writes[FurloughEmployeesQuestion] {
    def writes(furloughEmployeesQuestion: FurloughEmployeesQuestion) =
      Json.toJson(furloughEmployeesQuestion.toString)
  }

  implicit object FurloughEmployeesQuestionReads extends Reads[FurloughEmployeesQuestion] {
    override def reads(json: JsValue): JsResult[FurloughEmployeesQuestion] = json match {
      case JsString(YesFurloughedMostEmployees.toString) =>
        JsSuccess(YesFurloughedMostEmployees)
      case JsString(YesFurloughedSomeEmployees.toString)   => JsSuccess(YesFurloughedSomeEmployees)
      case JsString(NoDidnotFurloughAnyEmployees.toString) => JsSuccess(NoDidnotFurloughAnyEmployees)
      case _                                               => JsError("Unknown FurloughEmployeesQuestion")
    }
  }
}
