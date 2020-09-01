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

sealed trait ProtectAtRiskJobsQuestion

object ProtectAtRiskJobsQuestion {

  case object Yes extends WithName("Yes") with ProtectAtRiskJobsQuestion
  case object No extends WithName("No") with ProtectAtRiskJobsQuestion

  val values: Seq[ProtectAtRiskJobsQuestion] = List(Yes, No)

  val options: Seq[RadioOption] = values.map { value =>
    RadioOption("protectAtRiskJobs", value.toString)
  }

  implicit val enumerable: Enumerable[ProtectAtRiskJobsQuestion] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object ProtectAtRiskJobsQuestion extends Writes[ProtectAtRiskJobsQuestion] {
    def writes(ProtectAtRiskJobsQuestion: ProtectAtRiskJobsQuestion) =
      Json.toJson(ProtectAtRiskJobsQuestion.toString)
  }

  implicit object ProtectAtRiskJobsQuestionReads extends Reads[ProtectAtRiskJobsQuestion] {
    override def reads(json: JsValue): JsResult[ProtectAtRiskJobsQuestion] = json match {
      case JsString(Yes.toString) => JsSuccess(Yes)
      case JsString(No.toString)  => JsSuccess(No)
      case _                      => JsError("Unknown ProtectAtRiskJobsQuestion")
    }
  }

}
