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
import models.ccg._
import models.eotho._
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen._
import org.scalacheck.{Arbitrary, Gen}
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

  implicit def arbitraryFeedbackId: Arbitrary[FeedbackId] =
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

  implicit lazy val arbitraryCcgQuesions: Arbitrary[CCGQuestions] = Arbitrary(ccgQuestionsGen)

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
      numberOfEstablishments             <- option(numberOfEstablishmentsGen)
      numberOfEmployees                  <- option(numberOfEmployeesGen)
      whichRegions                       <- arbitrary[List[WhichRegionQuestion]]
      affectedJobs                       <- option(affectedJobs)
      protectAtRiskJobs                  <- option(arbitrary[Boolean])
      protectHospitalityIndustry         <- option(arbitrary[Boolean])
      compareToMonTueWed                 <- option(comparedToMonTueWed)
      comparedToThurFriSatSun            <- option(comparedToThurFriSatSun)
      comparedBusinessTurnOver           <- option(comparedBusinessTurnover)
      encourageReopenSooner              <- option(arbitrary[Boolean])
      encourageReturnToRestaurantsSooner <- option(arbitrary[Boolean])
      offerDiscounts                     <- option(offerDiscounts)
      businessFuturePlans                <- option(businessFuturePlans)
    } yield {
      EOTHOQuestions(
        numberOfEstablishments,
        numberOfEmployees,
        whichRegions,
        affectedJobs,
        protectAtRiskJobs,
        protectHospitalityIndustry,
        compareToMonTueWed,
        comparedToThurFriSatSun,
        comparedBusinessTurnOver,
        encourageReopenSooner,
        encourageReturnToRestaurantsSooner,
        offerDiscounts,
        businessFuturePlans
      )
    }

  lazy val ccgQuestionsGen: Gen[CCGQuestions] =
    for {
      complianceCheckUnderstanding <- option(complianceCheckUnderstandingGen)
      treatedProfessionally        <- option(treatedProfessionallyGen)
    } yield
      CCGQuestions(
        complianceCheckUnderstanding,
        treatedProfessionally
      )

  lazy val complianceCheckUnderstandingGen: Gen[ComplianceCheckUnderstandingQuestion] =
    oneOf(ComplianceCheckUnderstandingQuestion.values)

  lazy val treatedProfessionallyGen: Gen[TreatedProfessionallyQuestion] =
    oneOf(TreatedProfessionallyQuestion.values)

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

  implicit lazy val arbitraryNumberOfEstablishments: Arbitrary[NumberOfEstablishmentsQuestion] =
    Arbitrary(numberOfEstablishmentsGen)

  lazy val numberOfEmployeesGen: Gen[NumberOfEmployeesQuestion] =
    oneOf(NumberOfEmployeesQuestion.values)

  implicit lazy val arbitraryNumberOfEmployees: Arbitrary[NumberOfEmployeesQuestion] =
    Arbitrary(numberOfEmployeesGen)

  lazy val comparedToMonTueWed: Gen[ComparedToMonTueWedQuestion] =
    oneOf(ComparedToMonTueWedQuestion.values)

  implicit lazy val arbitraryComparedToMonTueWed: Arbitrary[ComparedToMonTueWedQuestion] =
    Arbitrary(comparedToMonTueWed)

  lazy val comparedToThurFriSatSun: Gen[ComparedToThurFriSatSunQuestion] =
    oneOf(ComparedToThurFriSatSunQuestion.values)

  implicit lazy val arbitraryComparedToThurFriSatSun: Arbitrary[ComparedToThurFriSatSunQuestion] =
    Arbitrary(comparedToThurFriSatSun)

  lazy val comparedBusinessTurnover: Gen[ComparedBusinessTurnoverQuestion] =
    oneOf(ComparedBusinessTurnoverQuestion.values)

  implicit lazy val arbitraryComparedBusinessTurnover: Arbitrary[ComparedBusinessTurnoverQuestion] =
    Arbitrary(comparedBusinessTurnover)

  lazy val affectedJobs: Gen[AffectedJobsQuestion] =
    oneOf(AffectedJobsQuestion.values)

  implicit lazy val arbitraryAffectedJobs: Arbitrary[AffectedJobsQuestion] =
    Arbitrary(affectedJobs)

  lazy val businessFuturePlans: Gen[BusinessFuturePlansQuestion] =
    oneOf(BusinessFuturePlansQuestion.values)

  implicit lazy val arbitraryBusinessFuturePlans: Arbitrary[BusinessFuturePlansQuestion] =
    Arbitrary(businessFuturePlans)

  lazy val offerDiscounts: Gen[OfferDiscountsQuestion] =
    oneOf(OfferDiscountsQuestion.values)

  implicit lazy val arbitraryOfferDiscounts: Arbitrary[OfferDiscountsQuestion] =
    Arbitrary(offerDiscounts)

  lazy val whichRegionQuestionGen: Gen[WhichRegionQuestion] =
    oneOf(WhichRegionQuestion.values)

  implicit lazy val arbitraryWhichRegionQuestion: Arbitrary[WhichRegionQuestion] =
    Arbitrary(whichRegionQuestionGen)

  implicit lazy val listOfarbitraryWhichRegionQuestion: Arbitrary[List[WhichRegionQuestion]] =
    Arbitrary(Gen.listOf(whichRegionQuestionGen))

  //ccg

  implicit lazy val arbitraryComplianceCheckUnderstandingQuestionSpec: Arbitrary[ComplianceCheckUnderstandingQuestion] =
    Arbitrary(complianceCheckUnderstandingGen)

  implicit lazy val treatedProfessionallyQuestionSpec: Arbitrary[TreatedProfessionallyQuestion] =
    Arbitrary(treatedProfessionallyGen)
}
