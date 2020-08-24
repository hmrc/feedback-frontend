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

import viewmodels.RadioOption

sealed trait WhichRegion

object WhichRegionQuestion extends Enumerable.Implicits {

  case object EastMidlands extends WithName("EastMidlands") with WhichRegion
  case object EastOfEngland extends WithName("EastOfEngland") with WhichRegion
  case object London extends WithName("London") with WhichRegion
  case object NorthEast extends WithName("NorthEast") with WhichRegion
  case object NorthWest extends WithName("NorthWest") with WhichRegion
  case object SouthEast extends WithName("SouthEast") with WhichRegion
  case object SouthWest extends WithName("SouthWest") with WhichRegion
  case object WestMidlands extends WithName("WestMidlands") with WhichRegion
  case object YorkshireAndHumber extends WithName("YorkshireAndHumber") with WhichRegion
  case object NorthernIreland extends WithName("NorthernIreland") with WhichRegion
  case object Scotland extends WithName("Scotland") with WhichRegion
  case object Wales extends WithName("Wales") with WhichRegion

  val values: Seq[WhichRegion] = Seq(
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
    RadioOption("whichRegion", value.toString)
  }

  implicit val enumerable: Enumerable[WhichRegion] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
