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

sealed trait AffectedJobsQuestion

object AffectedJobsQuestion extends Enumerable.Implicits {

  case object KeepAllAndTakeOnAdditionalStaff
      extends WithName("KeepAllAndTakeOnAdditionalStaff") with AffectedJobsQuestion
  case object KeepAllOrMostJobs extends WithName("KeepAllOrMostJobs") with AffectedJobsQuestion
  case object KeepSomeJobs extends WithName("KeepSomeJobs") with AffectedJobsQuestion
  case object KeepNoJobs extends WithName("KeepNoJobs") with AffectedJobsQuestion
  case object NotApplicableNobodyEmployed extends WithName("NotApplicableNobodyEmployed") with AffectedJobsQuestion

  val values: Seq[AffectedJobsQuestion] =
    List(KeepAllAndTakeOnAdditionalStaff, KeepAllOrMostJobs, KeepSomeJobs, KeepNoJobs, NotApplicableNobodyEmployed)

  val options: Seq[RadioOption] = values.map { value =>
    RadioOption("affectedJobs", value.toString)
  }

  implicit val enumerable: Enumerable[AffectedJobsQuestion] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
