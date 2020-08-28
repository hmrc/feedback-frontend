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

sealed trait BusinessFuturePlansQuestion

object BusinessFuturePlansQuestion {

  case object WePlanToStopTrading extends WithName("WePlanToStopTrading") with BusinessFuturePlansQuestion
  case object WePlanToReduceOurOpeningHoursButContinueToTrade
      extends WithName("WePlanToReduceOurOpeningHoursButContinueToTrade") with BusinessFuturePlansQuestion
  case object WePlanToContinueWithTheSameOpeningHours
      extends WithName("WePlanToContinueWithTheSameOpeningHours") with BusinessFuturePlansQuestion
  case object WePlanToIncreaseOurOpeningHoursOrOpenMoreEstablishments
      extends WithName("WePlanToIncreaseOurOpeningHoursOrOpenMoreEstablishments") with BusinessFuturePlansQuestion

  val values: Seq[BusinessFuturePlansQuestion] =
    List(
      WePlanToStopTrading,
      WePlanToReduceOurOpeningHoursButContinueToTrade,
      WePlanToContinueWithTheSameOpeningHours,
      WePlanToIncreaseOurOpeningHoursOrOpenMoreEstablishments
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
      case JsString(WePlanToStopTrading.toString) =>
        JsSuccess(WePlanToStopTrading)
      case JsString(WePlanToReduceOurOpeningHoursButContinueToTrade.toString) =>
        JsSuccess(WePlanToReduceOurOpeningHoursButContinueToTrade)
      case JsString(WePlanToContinueWithTheSameOpeningHours.toString) =>
        JsSuccess(WePlanToContinueWithTheSameOpeningHours)
      case JsString(WePlanToIncreaseOurOpeningHoursOrOpenMoreEstablishments.toString) =>
        JsSuccess(WePlanToIncreaseOurOpeningHoursOrOpenMoreEstablishments)
      case _ => JsError("Unknown BusinessFuturePlansQuestion")
    }
  }
}
