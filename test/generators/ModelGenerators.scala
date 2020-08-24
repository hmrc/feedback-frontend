/*
 * Copyright 2020 HM Revenue & Customs
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
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen._
import org.scalacheck.{Arbitrary, Gen}
import play.api.Application
import play.api.test.FakeRequest

trait ModelGenerators {

  implicit lazy val arbitraryGiveReason: Arbitrary[GiveReason] =
    Arbitrary {
      Gen.oneOf(GiveReason.values)
    }

  implicit lazy val arbitraryOrigin: Arbitrary[Origin] =
    Arbitrary {
      arbitrary[String].map(Origin.fromString)
    }

  implicit def arbitraryFeedbackId(implicit ev: Application): Arbitrary[FeedbackId] =
    Arbitrary {
      arbitrary[String].map { s =>
        FeedbackId.fromSession(FakeRequest("GET", "").withSession("feedbackId" -> s))
      }
    }

  implicit lazy val arbitraryOtherQuestions: Arbitrary[OtherQuestions] = Arbitrary(otherQuestionsGen)

  implicit lazy val arbitraryOtherEmployeeExpensesBetaQuestions: Arbitrary[OtherQuestionsEmployeeExpensesBeta] =
    Arbitrary(otherQuestionsEmployeeExpensesBetaGen)

  implicit lazy val arbitraryPTAQuestions: Arbitrary[PTAQuestions] = Arbitrary(ptaQuestionsGen)

  implicit lazy val arbitraryBTAQuestions: Arbitrary[BTAQuestions] = Arbitrary(btaQuestionsGen)

  implicit lazy val arbitraryPensionQuestions: Arbitrary[PensionQuestions] = Arbitrary(pensionQuestionsGen)

  implicit lazy val arbitraryEothoQuesions: Arbitrary[EOTHOQuestions] = Arbitrary(eothoQuestionsGen)

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
      OtherQuestionsEmployeeExpensesBeta(ableToDo, howEasy, whyScore, howFeel, fullName, email)
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

  lazy val eothoQuestionsGen: Gen[EOTHOQuestions] =
    for {
      numberOfEstablishments  <- option(numberOfEstablishmentsGen)
      whichRegions            <- arbitrary[List[WhichRegionQuestion]]
      compareToMonTueWed      <- option(comparedToMonTueWed)
      comparedToThurFriSatSun <- option(comparedToThurFriSatSun)
    } yield {
      EOTHOQuestions(numberOfEstablishments, whichRegions, compareToMonTueWed, comparedToThurFriSatSun)
    }

  lazy val howEasyQuestionGen: Gen[HowEasyQuestion] =
    oneOf(HowEasyQuestion.values)

  lazy val howDoYouFeelQuestionGen: Gen[HowDoYouFeelQuestion] =
    oneOf(HowDoYouFeelQuestion.values)

  lazy val mainServiceQuestionGen: Gen[MainServiceQuestion] =
    oneOf(MainServiceQuestion.values)

  lazy val likelyToDoQuestionGen: Gen[LikelyToDoQuestion] =
    oneOf(LikelyToDoQuestion.values)

  lazy val numberOfEstablishmentsGen: Gen[NumberOfEstablishmentsQuestion] =
    oneOf(NumberOfEstablishmentsQuestion.values)

  lazy val comparedToMonTueWed: Gen[ComparedToMonTueWedQuestion] =
    oneOf(ComparedToMonTueWedQuestion.values)

  lazy val comparedToThurFriSatSun: Gen[ComparedToThurFriSatSunQuestion] =
    oneOf(ComparedToThurFriSatSunQuestion.values)

  lazy val whichRegionQuestionGen: Gen[WhichRegionQuestion] =
    oneOf(WhichRegionQuestion.values)

  implicit lazy val arbitraryWhichRegionQuestion: Arbitrary[WhichRegionQuestion] =
    Arbitrary {
      Gen.oneOf(WhichRegionQuestion.values)
    }

  implicit lazy val listOfarbitraryWhichRegionQuestion: Arbitrary[List[WhichRegionQuestion]] =
    Arbitrary {
      Gen.listOf(whichRegionQuestionGen)
    }
}
