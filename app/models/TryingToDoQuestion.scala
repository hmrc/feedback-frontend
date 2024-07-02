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

package models

import play.api.data.Form
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.radios.RadioItem

sealed trait TryingToDoQuestion

object TryingToDoQuestion {

  val baseMessageKey: String = "TryingToDoQuestion"

  private case object RegisterATrust extends WithName("RegisterATrust") with TryingToDoQuestion

  private case object ClaimATrust extends WithName("ClaimATrust") with TryingToDoQuestion

  private case object CloseATrust extends WithName("CloseATrust") with TryingToDoQuestion

  private case object MaintainATrust extends WithName("MaintainATrust") with TryingToDoQuestion

  private case object GetEvidenceOfRegistration extends WithName("GetEvidenceOfRegistration") with TryingToDoQuestion

  private case object Other extends WithName("Other") with TryingToDoQuestion

  val values: Seq[TryingToDoQuestion] =
    List(RegisterATrust, ClaimATrust, CloseATrust, MaintainATrust, GetEvidenceOfRegistration, Other)

  def options(form: Form[_])(implicit messages: Messages): Seq[RadioItem] = values.map { value =>
    RadioItem(
      id = Some(s"$baseMessageKey-${value.toString}"),
      value = Some(value.toString),
      content = Text(messages(s"$baseMessageKey.$value")),
      checked = form(baseMessageKey).value.contains(value.toString)
    )
  }

  implicit val enumerable: Enumerable[TryingToDoQuestion] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
