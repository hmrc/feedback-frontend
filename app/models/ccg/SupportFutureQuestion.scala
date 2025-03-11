/*
 * Copyright 2024 HM Revenue & Customs
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
import play.api.data.Form
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.radios.RadioItem

trait SupportFutureQuestion

object SupportFutureQuestion extends Enumerable.Implicits {

  val baseMessageKey: String = "supportFutureQuestion"

  private case object VeryConfident extends WithName("VeryConfident") with SupportFutureQuestion

  private case object FairlyConfident extends WithName("FairlyConfident") with SupportFutureQuestion

  private case object Neutral extends WithName("Neutral") with SupportFutureQuestion

  private case object NotVeryConfident extends WithName("NotVeryConfident") with SupportFutureQuestion

  private case object NotAtAllConfident extends WithName("NotAtAllConfident") with SupportFutureQuestion

  val values: Seq[SupportFutureQuestion] =
    List(VeryConfident, FairlyConfident, Neutral, NotVeryConfident, NotAtAllConfident)

  def options(form: Form[?])(implicit messages: Messages): Seq[RadioItem] = values.map { value =>
    RadioItem(
      id = Some(s"$baseMessageKey-${value.toString}"),
      value = Some(value.toString),
      content = Text(messages(s"$baseMessageKey.$value")),
      checked = form(baseMessageKey).value.contains(value.toString)
    )
  }

  implicit val enumerable: Enumerable[SupportFutureQuestion] =
    Enumerable(values.map(v => v.toString -> v) *)
}
