/*
 * Copyright 2019 HM Revenue & Customs
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

package generators

import models._
import org.scalacheck.{Arbitrary, Gen}
import Arbitrary._
import Gen._

trait ModelGenerators {

  implicit lazy val arbitraryGiveReason: Arbitrary[GiveReason] =
    Arbitrary {
      Gen.oneOf(GiveReason.values)
    }

  implicit lazy val arbitraryOtherQuestions: Arbitrary[OtherQuestions] = Arbitrary(otherQuestionsGen)

  implicit lazy val arbitraryOtherEmployeeExpensesBetaQuestions: Arbitrary[OtherQuestionsEmployeeExpensesBeta] = Arbitrary(otherQuestionsEmployeeExpensesBetaGen)

  implicit lazy val arbitraryPTAQuestions: Arbitrary[PTAQuestions] = Arbitrary(ptaQuestionsGen)

  implicit lazy val arbitraryBTAQuestions: Arbitrary[BTAQuestions] = Arbitrary(btaQuestionsGen)

  implicit lazy val arbitraryPensionQuestions: Arbitrary[PensionQuestions] = Arbitrary(pensionQuestionsGen)

  implicit lazy val arbitraryGiveReasonQuestions: Arbitrary[GiveReasonQuestions] = Arbitrary {
    for {
      value  <- option(arbitrary[GiveReason])
      reason <- option(arbitrary[String].suchThat(_.nonEmpty))
    } yield {
      GiveReasonQuestions(value, reason)
    }
  }

  lazy val otherQuestionsGen: Gen[OtherQuestions] =
    for {
      ableToDo <- option(arbitrary[Boolean])
      howEasy  <- option(howEasyQuestionGen)
      whyScore <- option(arbitrary[String].suchThat(_.nonEmpty))
      howFeel  <- option(howDoYouFeelQuestionGen)
    } yield {
      OtherQuestions(ableToDo, howEasy, whyScore, howFeel)
    }


  lazy val otherQuestionsEmployeeExpensesBetaGen: Gen[OtherQuestionsEmployeeExpensesBeta] =
    for {
      ableToDo <- option(arbitrary[Boolean])
      howEasy  <- option(howEasyQuestionGen)
      whyScore <- option(arbitrary[String].suchThat(_.nonEmpty))
      howFeel  <- option(howDoYouFeelQuestionGen)
      fullName <- option(arbitrary[String].suchThat(_.nonEmpty))
      email    <- option(arbitrary[String].suchThat(_.nonEmpty))
    } yield {
      val personalDetails=PersonalDetails(fullName,email)
      OtherQuestionsEmployeeExpensesBeta(ableToDo, howEasy, whyScore, howFeel, Some(personalDetails))
    }
  lazy val ptaQuestionsGen: Gen[PTAQuestions] =
    for {
      neededToDo <- option(arbitrary[String].suchThat(_.nonEmpty))
      ableToDo   <- option(arbitrary[Boolean])
      howEasy    <- option(howEasyQuestionGen)
      whyScore   <- option(arbitrary[String].suchThat(_.nonEmpty))
      howFeel    <- option(howDoYouFeelQuestionGen)
    } yield {
      PTAQuestions(neededToDo, ableToDo, howEasy, whyScore, howFeel)
    }

  lazy val btaQuestionsGen: Gen[BTAQuestions] =
    for {
      mainService      <- option(mainServiceQuestionGen)
      mainServiceOther <- option(arbitrary[String].suchThat(_.nonEmpty))
      ableToDo         <- option(arbitrary[Boolean])
      howEasy          <- option(howEasyQuestionGen)
      whyScore         <- option(arbitrary[String].suchThat(_.nonEmpty))
      howFeel          <- option(howDoYouFeelQuestionGen)
    } yield {
      BTAQuestions(mainService, mainServiceOther, ableToDo, howEasy, whyScore, howFeel)
    }

  lazy val pensionQuestionsGen: Gen[PensionQuestions] =
    for {
      ableToDo   <- option(arbitrary[Boolean])
      howEasy    <- option(howEasyQuestionGen)
      whyScore   <- option(arbitrary[String].suchThat(_.nonEmpty))
      howFeel    <- option(howDoYouFeelQuestionGen)
      likelyToDo <- option(likelyToDoQuestionGen)
    } yield {
      PensionQuestions(ableToDo, howEasy, whyScore, howFeel, likelyToDo)
    }

  lazy val howEasyQuestionGen: Gen[HowEasyQuestion] =
    oneOf(HowEasyQuestion.values)

  lazy val howDoYouFeelQuestionGen: Gen[HowDoYouFeelQuestion] =
    oneOf(HowDoYouFeelQuestion.values)

  lazy val mainServiceQuestionGen: Gen[MainServiceQuestion] =
    oneOf(MainServiceQuestion.values)

  lazy val likelyToDoQuestionGen: Gen[LikelyToDoQuestion] =
    oneOf(LikelyToDoQuestion.values)
}
