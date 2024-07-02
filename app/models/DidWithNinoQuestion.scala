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
import uk.gov.hmrc.govukfrontend.views.Aliases.{ExclusiveCheckbox, Text}
import uk.gov.hmrc.govukfrontend.views.viewmodels.checkboxes.CheckboxItem

sealed trait DidWithNinoQuestion

object DidWithNinoQuestion {

  val baseMessageKey: String = "didWithNinoQuestion"

  private case object WroteOnPaper extends WithName("wroteOnPaper") with DidWithNinoQuestion

  private case object WroteOnDevice extends WithName("wroteOnDevice") with DidWithNinoQuestion

  private case object ScreenshotPage extends WithName("screenshotPage") with DidWithNinoQuestion

  private case object PrintedIt extends WithName("printedIt") with DidWithNinoQuestion

  private case object DownloadedIt extends WithName("downloadedIt") with DidWithNinoQuestion

  private case object SavedToWallet extends WithName("savedToWallet") with DidWithNinoQuestion

  private case object VisualConfirmation extends WithName("visualConfirmation") with DidWithNinoQuestion

  private case object Divider extends WithName("divider") with DidWithNinoQuestion

  private case object NoneOfAbove extends WithName("noneOfAbove") with DidWithNinoQuestion

  val values: Seq[DidWithNinoQuestion] =
    List(WroteOnPaper, WroteOnDevice, ScreenshotPage, PrintedIt, DownloadedIt, SavedToWallet, VisualConfirmation, Divider, NoneOfAbove)

  def options(form: Form[_])(implicit messages: Messages): Seq[CheckboxItem] = values.map { value =>
    if (value.toString.equals("divider")) {
      CheckboxItem(
        divider = Some(messages(s"$baseMessageKey.$value"))
      )
    }
    else if (value.toString.equals("noneOfAbove")) {
      CheckboxItem(
        content = Text(messages(s"$baseMessageKey.$value")),
        value = value.toString,
        behaviour = Some(ExclusiveCheckbox)
      )
    }
    else {
      CheckboxItem(
        id = Some(s"$baseMessageKey-${value.toString}"),
        value = value.toString,
        content = Text(messages(s"$baseMessageKey.$value")),
        checked = form(baseMessageKey).value.contains(value.toString)
      )
    }
  }

  implicit val enumerable: Enumerable[DidWithNinoQuestion] =
    Enumerable(values.map(v => v.toString -> v): _*)
}