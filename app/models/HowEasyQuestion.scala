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

sealed trait HowEasyQuestion {
  val value: Int
}

object HowEasyQuestion {

  val baseMessageKey: String = "howEasyQuestion"

  case object VeryEasy extends WithName("VeryEasy") with HowEasyQuestion {
    val value = 5
  }
  case object Easy extends WithName("Easy") with HowEasyQuestion {
    val value = 4
  }
  case object Moderate extends WithName("Moderate") with HowEasyQuestion {
    val value = 3
  }
  case object Difficult extends WithName("Difficult") with HowEasyQuestion {
    val value = 2
  }
  case object VeryDifficult extends WithName("VeryDifficult") with HowEasyQuestion {
    val value = 1
  }

  val values: Seq[HowEasyQuestion] =
    List(VeryEasy, Easy, Moderate, Difficult, VeryDifficult)

  def options(form: Form[_])(implicit messages: Messages): Seq[RadioItem] = values.map { value =>
    RadioItem(
      id = Some(s"$baseMessageKey-${value.toString}"),
      value = Some(value.toString),
      content = Text(messages(s"$baseMessageKey.$value")),
      checked = form(baseMessageKey).value.contains(value.toString)
    )
  }

  implicit val enumerable: Enumerable[HowEasyQuestion] =
    Enumerable(values.map(v => v.toString -> v): _*)

}
