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

package base

import models._
import models.ccg.{CheckUnderstandingQuestion, SupportFutureQuestion, TreatedProfessionallyQuestion}

object CommonSpecValues {

  val serviceNames: List[String] = List(
    "",
    "Check-Your-State-Pension",
    "P800",
    "Tax-Allowance-Married-Couples",
    "Pension-Annual-Allowance-Calculator",
    "Tax-Credits-Renewals",
    "Tax-Credits-Service",
    "TaxCalc",
    "Repayments",
    "Child-Benefit-View"
  )

  val feedbackIDs: List[String] = List(
    "ID_Julius_0270",
    "ID_Caesar_1574",
    "ID_Brutus_9191",
    "ID_Romeo_1010",
    "ID_Juliet_0101"
  )

  val clientIDs: List[String] = List(
    "1234567",
    "0102030",
    "02468",
    "013579",
    "7777777"
  )

  val invalidAnswers: List[String] = List(
    "",
    "Oh Dear !!",
    "9876543210 Lift Off !",
    "±!@£$%^&*()_+",
    "ABCDEFG hijklmn",
    "-={}[]|;:'.,?><`~",
    "すばらしい こと だ",
    "備えあれば憂いなし"
  )

  val invalidFormFieldValues: List[String] = List(
    "مصائب قوم عند قوم فوائد",
    "±!@£$%^&*()_+",
    "-={}[]|;:'.,?><`~",
    "تجري الرياح بما لا تشتهي السّفن",
    "備えあれば憂いなし",
    "男に二言はない",
    "الحاجة أمّ الاختراع",
    "火のないところに煙は立たない",
    "出る杭は打たれる",
    "لا تؤجّل عمل اليوم إلى الغد"
  )

  val mainServiceQuestionNumberOfOptions: Int           = MainServiceQuestion.values.length
  val ableToDoQuestionNumberOfOptions: Int              = AbleToDo.values.length
  val howEasyQuestionNumberOfOptions: Int               = HowEasyQuestion.values.length
  val howDoYouFeelQuestionNumberOfOptions: Int          = HowDoYouFeelQuestion.values.length
  val checkUnderstandingQuestionNumberOfOptions: Int    = CheckUnderstandingQuestion.values.length
  val treatedProfessionallyQuestionNumberOfOptions: Int = TreatedProfessionallyQuestion.values.length
  val supportFutureQuestionNumberOfOptions: Int         = SupportFutureQuestion.values.length
  val yesNoQuestionNumberOfOptions: Int                 = YesNo.values.length
  val tryingToDoQuestionNumberOfOptions: Int            = TryingToDoQuestion.values.length
  val didWithNinoQuestionNumberOfOptions: Int           = DidWithNinoQuestion.values.length
  val likelyToDoQuestionNumberOfOptions: Int            = LikelyToDoQuestion.values.length
  val giveReasonNumberOfOptions: Int                    = GiveReason.values.length

}
