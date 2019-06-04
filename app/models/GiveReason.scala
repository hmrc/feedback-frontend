/*
 * Copyright 2019 HM Revenue & Customs
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

sealed trait GiveReason

object GiveReason {

  case object CheckTaxCode     extends WithName("checkTaxCode")     with GiveReason
  case object CheckTaxYear     extends WithName("checkTaxYear")     with GiveReason
  case object CheckTaxPaid     extends WithName("checkTaxPaid")     with GiveReason
  case object ClaimTaxBack     extends WithName("claimTaxBack")     with GiveReason
  case object ContactAboutP800 extends WithName("contactAboutP800") with GiveReason
  case object P800Wrong        extends WithName("p800Wrong")        with GiveReason
  case object PayOwedTax       extends WithName("payOwedTax")       with GiveReason
  case object ProgressChasing  extends WithName("progressChasing")  with GiveReason
  case object Other            extends WithName("other")            with GiveReason

  val values: Seq[GiveReason] = Seq(
    CheckTaxCode, CheckTaxYear, CheckTaxPaid, ClaimTaxBack, ContactAboutP800, P800Wrong, PayOwedTax, ProgressChasing, Other
  )

  val options: Seq[RadioOption] = values.map {
    value =>
      RadioOption("giveReason", value.toString)
  }

  implicit val enumerable: Enumerable[GiveReason] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object GiveReasonWrites extends Writes[GiveReason] {
    def writes(giveReason: GiveReason) = Json.toJson(giveReason.toString)
  }

  implicit object GiveReasonReads extends Reads[GiveReason] {
    override def reads(json: JsValue): JsResult[GiveReason] = json match {
      case JsString(CheckTaxCode.toString)     => JsSuccess(CheckTaxCode)
      case JsString(CheckTaxYear.toString)     => JsSuccess(CheckTaxYear)
      case JsString(CheckTaxPaid.toString)     => JsSuccess(CheckTaxPaid)
      case JsString(ClaimTaxBack.toString)     => JsSuccess(ClaimTaxBack)
      case JsString(ContactAboutP800.toString) => JsSuccess(ContactAboutP800)
      case JsString(P800Wrong.toString)        => JsSuccess(P800Wrong)
      case JsString(PayOwedTax.toString)       => JsSuccess(PayOwedTax)
      case JsString(ProgressChasing.toString)  => JsSuccess(ProgressChasing)
      case JsString(Other.toString)            => JsSuccess(Other)
      case _                                   => JsError("Unknown giveReason")
    }
  }

}
