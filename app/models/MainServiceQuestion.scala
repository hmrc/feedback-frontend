/*
 * Copyright 2022 HM Revenue & Customs
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

import play.api.data.Form
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.radios.RadioItem

sealed trait MainServiceQuestion

object MainServiceQuestion {

  val baseMessageKey: String = "mainServiceQuestion"

  case object SelfAssesment extends WithName("SelfAssesment") with MainServiceQuestion
  case object PAYE extends WithName("PAYE") with MainServiceQuestion
  case object VAT extends WithName("VAT") with MainServiceQuestion
  case object CorporationTax extends WithName("CorporationTax") with MainServiceQuestion
  case object CIS extends WithName("CIS") with MainServiceQuestion
  case object ECSales extends WithName("ECSales") with MainServiceQuestion
  case object Other extends WithName("Other") with MainServiceQuestion

  val values: Seq[MainServiceQuestion] =
    List(SelfAssesment, PAYE, VAT, CorporationTax, CIS, ECSales, Other)

  def options(form: Form[_])(implicit messages: Messages): Seq[RadioItem] = values.map { value =>
    RadioItem(
      id = Some(s"$baseMessageKey-${value.toString}"),
      value = Some(value.toString),
      content = Text(messages(s"$baseMessageKey.$value")),
      checked = form(baseMessageKey).value.contains(value.toString)
    )
  }

  implicit val enumerable: Enumerable[MainServiceQuestion] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
