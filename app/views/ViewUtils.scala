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

package views

import models._
import play.api.data.Form
import play.api.i18n.Messages

object ViewUtils {

  def errorPrefix(form: Form[?])(implicit messages: Messages): String =
    if (form.hasErrors || form.hasGlobalErrors) messages("error.browser.title.prefix") else ""

  def errorLinkId(key: String, form: Form[?])(implicit messages: Messages): String = key match {
    case value if value.contains("mainService")       => s"#${MainServiceQuestion.options(form).head.id.getOrElse("")}"
    case value if value.contains("ableToDo")          => s"#${AbleToDo.options(form).head.id.getOrElse("")}"
    case value if value.contains("howEasyScore")      => s"#${HowEasyQuestion.options(form).head.id.getOrElse("")}"
    case value if value.contains("howDoYouFeelScore") => s"#${HowDoYouFeelQuestion.options(form).head.id.getOrElse("")}"
    case value if value.contains("likelyToDo")        => s"#${LikelyToDoQuestion.options(form).head.id.getOrElse("")}"
    case value                                        => s"#$value"
  }
}
