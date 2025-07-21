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

import models.ccg._

case class OtherQuestions(
  ableToDo: Option[AbleToDo],
  howEasyScore: Option[HowEasyQuestion],
  whyGiveScore: Option[String],
  howDoYouFeelScore: Option[HowDoYouFeelQuestion]
)

case class NinoQuestions(
  ableToDo: Option[AbleToDo],
  howEasyScore: Option[HowEasyQuestion],
  whyGiveScore: Option[String],
  howDoYouFeelScore: Option[HowDoYouFeelQuestion],
  logInToSeeNino: Option[YesNo],
  didWithNino: Option[Seq[DidWithNinoQuestion]],
  whyGiveAnswer: Option[String]
)

case class PTAQuestions(
  neededToDo: Option[String],
  ableToDo: Option[AbleToDo],
  howEasyScore: Option[HowEasyQuestion],
  whyGiveScore: Option[String],
  howDoYouFeelScore: Option[HowDoYouFeelQuestion]
)

case class BTAQuestions(
  mainService: Option[MainServiceQuestion],
  mainServiceOther: Option[String],
  ableToDo: Option[AbleToDo],
  howEasyScore: Option[HowEasyQuestion],
  whyGiveScore: Option[String],
  howDoYouFeelScore: Option[HowDoYouFeelQuestion]
)

case class TrustsQuestions(
  isAgent: Option[YesNo],
  tryingToDo: Option[TryingToDoQuestion],
  tryingToDoOther: Option[String],
  ableToDo: Option[AbleToDo],
  whyNotAbleToDo: Option[String],
  howEasyScore: Option[HowEasyQuestion],
  whyGiveScore: Option[String],
  howDoYouFeelScore: Option[HowDoYouFeelQuestion]
)

case class PensionQuestions(
  ableToDo: Option[AbleToDo],
  howEasyScore: Option[HowEasyQuestion],
  whyGiveScore: Option[String],
  howDoYouFeelScore: Option[HowDoYouFeelQuestion],
  likelyToDo: Option[LikelyToDoQuestion]
)

case class CCGQuestions(
  complianceCheckUnderstanding: Option[CheckUnderstandingQuestion],
  treatedProfessionally: Option[TreatedProfessionallyQuestion],
  whyGiveAnswer: Option[String],
  supportFutureTaxQuestion: Option[SupportFutureQuestion]
)

case class NmwCcgQuestions(
  treatedProfessionally: Option[TreatedProfessionallyQuestion],
  checkUnderstanding: Option[CheckUnderstandingQuestion],
  whyGiveAnswer: Option[String],
  supportFutureNmw: Option[SupportFutureQuestion]
)

case class ComplaintFeedbackQuestions(
  complaintHandledFairly: Option[YesNo],
  howEasyScore: Option[HowEasyQuestion],
  whyGiveScore: Option[String],
  howDoYouFeelScore: Option[HowDoYouFeelQuestion]
)

case class GiveReasonQuestions(value: Option[GiveReason], reason: Option[String])
