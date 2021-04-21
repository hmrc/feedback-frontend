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

sealed trait TryingToDoQuestion

object TryingToDoQuestion {

  case object RegisterATrust extends WithName("RegisterATrust") with TryingToDoQuestion
  case object ClaimATrust extends WithName("ClaimATrust") with TryingToDoQuestion
  case object CloseATrust extends WithName("CloseATrust") with TryingToDoQuestion
  case object MaintainATrust extends WithName("MaintainATrust") with TryingToDoQuestion
  case object GetEvidenceOfRegistration extends WithName("GetEvidenceOfRegistration") with TryingToDoQuestion
  case object Other extends WithName("Other") with TryingToDoQuestion

  val values: Seq[TryingToDoQuestion] =
    List(RegisterATrust, ClaimATrust, CloseATrust, MaintainATrust, GetEvidenceOfRegistration, Other)

  val options: Seq[RadioOption] = values.map { value =>
    RadioOption("TryingToDoQuestion", value.toString)
  }

  implicit val enumerable: Enumerable[TryingToDoQuestion] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object TryingToDoQuestionWrites extends Writes[TryingToDoQuestion] {
    def writes(TryingToDoQuestion: TryingToDoQuestion) = Json.toJson(TryingToDoQuestion.toString)
  }

  implicit object TryingToDoQuestionReads extends Reads[TryingToDoQuestion] {
    override def reads(json: JsValue): JsResult[TryingToDoQuestion] = json match {
      case JsString(RegisterATrust.toString)            => JsSuccess(RegisterATrust)
      case JsString(ClaimATrust.toString)               => JsSuccess(ClaimATrust)
      case JsString(CloseATrust.toString)               => JsSuccess(CloseATrust)
      case JsString(MaintainATrust.toString)            => JsSuccess(MaintainATrust)
      case JsString(GetEvidenceOfRegistration.toString) => JsSuccess(GetEvidenceOfRegistration)
      case JsString(Other.toString)                     => JsSuccess(Other)
      case _                                            => JsError("Unknown TryingToDoQuestion")
    }
  }
}
