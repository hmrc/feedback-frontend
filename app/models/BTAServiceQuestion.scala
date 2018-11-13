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

import play.api.libs.json.{JsValue, _}
import viewmodels.RadioOption

sealed trait BTAServiceQuestion

object BTAServiceQuestion {

  case object SelfAssessment extends WithName("SelfAssessment") with BTAServiceQuestion
  case object PAYE           extends WithName("PAYE") with BTAServiceQuestion
  case object VAT            extends WithName("VAT") with BTAServiceQuestion
  case object CorporationTax extends WithName("CorporationTax") with BTAServiceQuestion
  case object CIS            extends WithName("CIS") with BTAServiceQuestion
  case object ECSales        extends WithName("ECSales") with BTAServiceQuestion
  case object Other          extends WithName("Other") with BTAServiceQuestion

  val values: Seq[BTAServiceQuestion] =
    List(SelfAssessment, PAYE, VAT, CorporationTax, CIS, ECSales, Other)

  val options: Seq[RadioOption] = values.map {
    value =>
      RadioOption("btaServiceQuestion", value.toString)
  }

  implicit val enumerable: Enumerable[BTAServiceQuestion] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object BTAServiceQuestionWrites extends Writes[BTAServiceQuestion] {
    def writes(btaServiceQuestion: BTAServiceQuestion) = Json.toJson(btaServiceQuestion.toString)
  }

  implicit object BTAServiceQuestionReads extends Reads[BTAServiceQuestion] {
    override def reads(json: JsValue): JsResult[BTAServiceQuestion] = json match {
      case JsString(SelfAssessment.toString) => JsSuccess(SelfAssessment)
      case JsString(PAYE.toString)           => JsSuccess(PAYE)
      case JsString(VAT.toString)            => JsSuccess(VAT)
      case JsString(CorporationTax.toString) => JsSuccess(CorporationTax)
      case JsString(CIS.toString)            => JsSuccess(CIS)
      case JsString(ECSales.toString)        => JsSuccess(ECSales)
      case JsString(Other.toString)          => JsSuccess(Other)
      case _                                 => JsError("Unknown btaServiceQuestion")
    }
  }
}

