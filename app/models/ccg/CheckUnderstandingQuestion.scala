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

package models.ccg

import models.{Enumerable, WithName}
import viewmodels.RadioOption

sealed trait CheckUnderstandingQuestion

object CheckUnderstandingQuestion extends Enumerable.Implicits {

  case object VeryEasy extends WithName("VeryEasy") with CheckUnderstandingQuestion
  case object Easy extends WithName("Easy") with CheckUnderstandingQuestion
  case object NeitherEasyOrDifficult extends WithName("NeitherEasyOrDifficult") with CheckUnderstandingQuestion
  case object Difficult extends WithName("Difficult") with CheckUnderstandingQuestion
  case object VeryDifficult extends WithName("VeryDifficult") with CheckUnderstandingQuestion

  val values: Seq[CheckUnderstandingQuestion] =
    List(VeryEasy, Easy, NeitherEasyOrDifficult, Difficult, VeryDifficult)

  val options: Seq[RadioOption] = values.map { value =>
    RadioOption("complianceCheckUnderstandingQuestion", value.toString)
  }

  implicit val enumerable: Enumerable[CheckUnderstandingQuestion] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
