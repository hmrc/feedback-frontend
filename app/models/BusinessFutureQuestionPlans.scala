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

sealed trait BusinessFuturePlansQuestion

object BusinessFuturePlansQuestion {

  case object PlanToPutAllEmployeesBackOnFurlough
      extends WithName("PlanToPutAllEmployeesBackOnFurlough") with BusinessFuturePlansQuestion
  case object PlanToPutSomeEmployeesBackOnFurloughButNotAll
      extends WithName("PlanToPutSomeEmployeesBackOnFurloughButNotAll") with BusinessFuturePlansQuestion
  case object DonotPlanToFurloughAnyEmployees
      extends WithName("DonotPlanToFurloughAnyEmployees") with BusinessFuturePlansQuestion

  val values: Seq[BusinessFuturePlansQuestion] =
    List(
      PlanToPutAllEmployeesBackOnFurlough,
      PlanToPutSomeEmployeesBackOnFurloughButNotAll,
      DonotPlanToFurloughAnyEmployees
    )

  val options: Seq[RadioOption] = values.map { value =>
    RadioOption("businessFuturePlans", value.toString)
  }

  implicit val enumerable: Enumerable[BusinessFuturePlansQuestion] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object BusinessFutureQuestion extends Writes[BusinessFuturePlansQuestion] {
    def writes(businessFutureQuestion: BusinessFuturePlansQuestion) =
      Json.toJson(businessFutureQuestion.toString)
  }

  implicit object BusinessFutureQuestionReads extends Reads[BusinessFuturePlansQuestion] {
    override def reads(json: JsValue): JsResult[BusinessFuturePlansQuestion] = json match {
      case JsString(PlanToPutAllEmployeesBackOnFurlough.toString) =>
        JsSuccess(PlanToPutAllEmployeesBackOnFurlough)
      case JsString(PlanToPutSomeEmployeesBackOnFurloughButNotAll.toString) =>
        JsSuccess(PlanToPutSomeEmployeesBackOnFurloughButNotAll)
      case _ => JsError("Unknown BusinessFuturePlansQuestion")
    }
  }
}
