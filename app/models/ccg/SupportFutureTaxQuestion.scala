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

package models.ccg

import models.{Enumerable, WithName}
import play.api.libs.json._
import viewmodels.RadioOption

trait SupportFutureTaxQuestion

object SupportFutureTaxQuestion extends Enumerable.Implicits {

  case object VeryConfident extends WithName("VeryConfident") with SupportFutureTaxQuestion
  case object FairlyConfident extends WithName("FairlyConfident") with SupportFutureTaxQuestion
  case object Neutral extends WithName("Neutral") with SupportFutureTaxQuestion
  case object NotVeryConfident extends WithName("NotVeryConfident") with SupportFutureTaxQuestion
  case object NotAtAllConfident extends WithName("NotAtAllConfident") with SupportFutureTaxQuestion

  val values: Seq[SupportFutureTaxQuestion] =
    List(VeryConfident, FairlyConfident, Neutral, NotVeryConfident, NotAtAllConfident)

  val options: Seq[RadioOption] = values.map { value =>
    RadioOption("supportFutureTaxQuestion", value.toString)
  }

  implicit val enumerable: Enumerable[SupportFutureTaxQuestion] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
