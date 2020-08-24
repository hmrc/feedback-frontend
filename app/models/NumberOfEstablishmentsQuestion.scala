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

sealed trait NumberOfEstablishmentsQuestion

object NumberOfEstablishmentsQuestion {

  case object FewerThan25 extends WithName("FewerThan25") with NumberOfEstablishmentsQuestion
  case object MoreThan25 extends WithName("MoreThan25") with NumberOfEstablishmentsQuestion

  val values: Seq[NumberOfEstablishmentsQuestion] =
    List(FewerThan25, MoreThan25)

  val options: Seq[RadioOption] = values.map { value =>
    RadioOption("numberOfEstablishmentsQuestion", value.toString)
  }

  implicit val enumerable: Enumerable[NumberOfEstablishmentsQuestion] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object NumberOfEstablishmentsQuestionWrites extends Writes[NumberOfEstablishmentsQuestion] {
    def writes(numberOfEstablishmentsQuestion: NumberOfEstablishmentsQuestion) =
      Json.toJson(numberOfEstablishmentsQuestion.toString)
  }

  implicit object NumberOfEstablishmentsQuestionReads extends Reads[NumberOfEstablishmentsQuestion] {
    override def reads(json: JsValue): JsResult[NumberOfEstablishmentsQuestion] = json match {
      case JsString(FewerThan25.toString) => JsSuccess(FewerThan25)
      case JsString(MoreThan25.toString)  => JsSuccess(MoreThan25)
      case _                              => JsError("Unknown NumberOfEstablishmentsQuestion")
    }
  }
}
