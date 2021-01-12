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

package models.eotho

import models.{Enumerable, WithName}
import viewmodels.RadioOption

sealed trait WhichRegionQuestion

object WhichRegionQuestion extends Enumerable.Implicits {

  case object EastMidlands extends WithName("EastMidlands") with WhichRegionQuestion
  case object EastOfEngland extends WithName("EastOfEngland") with WhichRegionQuestion
  case object London extends WithName("London") with WhichRegionQuestion
  case object NorthEast extends WithName("NorthEast") with WhichRegionQuestion
  case object NorthWest extends WithName("NorthWest") with WhichRegionQuestion
  case object SouthEast extends WithName("SouthEast") with WhichRegionQuestion
  case object SouthWest extends WithName("SouthWest") with WhichRegionQuestion
  case object WestMidlands extends WithName("WestMidlands") with WhichRegionQuestion
  case object YorkshireAndHumber extends WithName("YorkshireAndHumber") with WhichRegionQuestion
  case object NorthernIreland extends WithName("NorthernIreland") with WhichRegionQuestion
  case object Scotland extends WithName("Scotland") with WhichRegionQuestion
  case object Wales extends WithName("Wales") with WhichRegionQuestion

  val values: Seq[WhichRegionQuestion] = Seq(
    EastMidlands,
    EastOfEngland,
    London,
    NorthEast,
    NorthWest,
    SouthEast,
    SouthWest,
    WestMidlands,
    YorkshireAndHumber,
    NorthernIreland,
    Scotland,
    Wales
  )

  val options: Seq[RadioOption] = values.map { value =>
    RadioOption("whichRegions", value.toString)
  }

  implicit val enumerable: Enumerable[WhichRegionQuestion] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
