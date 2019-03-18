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

sealed trait LikelyToDoQuestion

object LikelyToDoQuestion {

  case object OtherPensions         extends WithName("OtherPensions") with LikelyToDoQuestion
  case object CheckFinances         extends WithName("CheckFinances") with LikelyToDoQuestion
  case object ClarifyInformation    extends WithName("ClarifyInformation") with LikelyToDoQuestion
  case object GetProfessionalAdvice extends WithName("GetProfessionalAdvice") with LikelyToDoQuestion
  case object DoNothing             extends WithName("DoNothing") with LikelyToDoQuestion

  val values: Seq[LikelyToDoQuestion] =
    List(OtherPensions, CheckFinances, ClarifyInformation, GetProfessionalAdvice, DoNothing)

  val options: Seq[RadioOption] = values.map {
    value =>
      RadioOption("likelyToDoQuestion", value.toString)
  }

  implicit val enumerable: Enumerable[LikelyToDoQuestion] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object LikelyToDoQuestionWrites extends Writes[LikelyToDoQuestion] {
    def writes(likelyToDoQuestion: LikelyToDoQuestion) = Json.toJson(likelyToDoQuestion.toString)
  }

  implicit object LikelyToDoQuestionReads extends Reads[LikelyToDoQuestion] {
    override def reads(json: JsValue): JsResult[LikelyToDoQuestion] = json match {
      case JsString(OtherPensions.toString)         => JsSuccess(OtherPensions)
      case JsString(CheckFinances.toString)         => JsSuccess(CheckFinances)
      case JsString(ClarifyInformation.toString)    => JsSuccess(ClarifyInformation)
      case JsString(GetProfessionalAdvice.toString) => JsSuccess(GetProfessionalAdvice)
      case JsString(DoNothing.toString)             => JsSuccess(DoNothing)
      case _                                        => JsError("Unknown PensionQuestion")
    }
  }
}
