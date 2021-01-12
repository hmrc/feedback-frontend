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
import play.api.libs.json._
import viewmodels.RadioOption

sealed trait OfferDiscountsQuestion

object OfferDiscountsQuestion extends Enumerable.Implicits {

  case object NoDoNotPlanToOfferDiscountsInSeptember
      extends WithName("NoDoNotPlanToOfferDiscountsInSeptember") with OfferDiscountsQuestion
  case object YesOfferUpTo50pcOnMonTueWedInSeptember
      extends WithName("YesOfferUpTo50pcOnMonTueWedInSeptember") with OfferDiscountsQuestion
  case object YesOfferDifferentDiscountsOrDealsInSeptember
      extends WithName("YesOfferDifferentDiscountsOrDealsInSeptember") with OfferDiscountsQuestion

  val values: Seq[OfferDiscountsQuestion] =
    List(
      NoDoNotPlanToOfferDiscountsInSeptember,
      YesOfferUpTo50pcOnMonTueWedInSeptember,
      YesOfferDifferentDiscountsOrDealsInSeptember
    )

  val options: Seq[RadioOption] = values.map { value =>
    RadioOption("offerDiscounts", value.toString)
  }

  implicit val enumerable: Enumerable[OfferDiscountsQuestion] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
