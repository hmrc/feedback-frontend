/*
 * Copyright 2021 HM Revenue & Customs
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

sealed trait MainServiceQuestion

object MainServiceQuestion {

  case object SelfAssesment extends WithName("SelfAssesment") with MainServiceQuestion
  case object PAYE extends WithName("PAYE") with MainServiceQuestion
  case object VAT extends WithName("VAT") with MainServiceQuestion
  case object CorporationTax extends WithName("CorporationTax") with MainServiceQuestion
  case object CIS extends WithName("CIS") with MainServiceQuestion
  case object ECSales extends WithName("ECSales") with MainServiceQuestion
  case object Other extends WithName("Other") with MainServiceQuestion

  val values: Seq[MainServiceQuestion] =
    List(SelfAssesment, PAYE, VAT, CorporationTax, CIS, ECSales, Other)

  val options: Seq[RadioOption] = values.map { value =>
    RadioOption("mainServiceQuestion", value.toString)
  }

  implicit val enumerable: Enumerable[MainServiceQuestion] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object MainServiceQuestionWrites extends Writes[MainServiceQuestion] {
    def writes(mainServiceQuestion: MainServiceQuestion) = Json.toJson(mainServiceQuestion.toString)
  }

  implicit object MainServiceQuestionReads extends Reads[MainServiceQuestion] {
    override def reads(json: JsValue): JsResult[MainServiceQuestion] = json match {
      case JsString(SelfAssesment.toString)  => JsSuccess(SelfAssesment)
      case JsString(PAYE.toString)           => JsSuccess(PAYE)
      case JsString(VAT.toString)            => JsSuccess(VAT)
      case JsString(CorporationTax.toString) => JsSuccess(CorporationTax)
      case JsString(CIS.toString)            => JsSuccess(CIS)
      case JsString(ECSales.toString)        => JsSuccess(ECSales)
      case JsString(Other.toString)          => JsSuccess(Other)
      case _                                 => JsError("Unknown MainServiceQuestion")
    }
  }
}
