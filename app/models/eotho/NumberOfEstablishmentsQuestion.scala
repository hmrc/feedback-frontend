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

sealed trait NumberOfEstablishmentsQuestion

object NumberOfEstablishmentsQuestion extends Enumerable.Implicits {

  case object OneEstablishment extends WithName("OneEstablishment") with NumberOfEstablishmentsQuestion
  case object TwoToTenEstablishments extends WithName("TwoToTenEstablishments") with NumberOfEstablishmentsQuestion
  case object TenOrMoreEstablishments extends WithName("TenOrMoreEstablishments") with NumberOfEstablishmentsQuestion
  case object NationalChainEstablishment
      extends WithName("NationalChainEstablishment") with NumberOfEstablishmentsQuestion

  val values: Seq[NumberOfEstablishmentsQuestion] =
    List(OneEstablishment, TwoToTenEstablishments, TenOrMoreEstablishments, NationalChainEstablishment)

  val options: Seq[RadioOption] = values.map { value =>
    RadioOption("numberOfEstablishmentsQuestion", value.toString)
  }

  implicit val enumerable: Enumerable[NumberOfEstablishmentsQuestion] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
