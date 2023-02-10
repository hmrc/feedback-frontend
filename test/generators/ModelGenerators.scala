/*
 * Copyright 2023 HM Revenue & Customs
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
      asciiStr.map { s =>
        FeedbackId.fromSession(FakeRequest("GET", "").withSession("feedbackId" -> s))
      }
    }

  implicit def arbitraryFeedbackCid: Arbitrary[Cid] =
    Arbitrary {
      numStr.map { s =>
        Cid.fromUrl(FakeRequest("GET", "").withHeaders("referer" -> s"/feedback/EXAMPLE?cid=$s"))
      }
    }

  implicit lazy val arbitraryOtherQuestions: Arbitrary[OtherQuestions] = Arbitrary(otherQuestionsGen)

  implicit lazy val arbitraryPTAQuestions: Arbitrary[PTAQuestions] = Arbitrary(ptaQuestionsGen)

  implicit lazy val arbitraryBTAQuestions: Arbitrary[BTAQuestions] = Arbitrary(btaQuestionsGen)

  implicit lazy val arbitraryTrustsQuestions: Arbitrary[TrustsQuestions] = Arbitrary(trustsQuestionsGen)

  implicit lazy val arbitraryPensionQuestions: Arbitrary[PensionQuestions] = Arbitrary(pensionQuestionsGen)

  implicit lazy val arbitraryCcgQuesions: Arbitrary[CCGQuestions] = Arbitrary(ccgQuestionsGen)

  implicit lazy val arbitraryNmwCcgQuesions: Arbitrary[NmwCcgQuestions] = Arbitrary(nmwCcgQuestionsGen)

  implicit lazy val arbitraryComplaintFeedbackQuestions: Arbitrary[ComplaintFeedbackQuestions] = Arbitrary(complaintFeedbackQuestionsGen)

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
      ableToDo <- option(ableToDoGen)
      howEasy  <- option(howEasyQuestionGen)
      whyScore <- option(arbitrary[String].suchThat(_.nonEmpty))
      howFeel  <- option(howDoYouFeelQuestionGen)
    } yield {
      OtherQuestions(ableToDo, howEasy, whyScore, howFeel)
    }

  lazy val ptaQuestionsGen: Gen[PTAQuestions] =
    for {
      neededToDo <- option(arbitrary[String].suchThat(_.nonEmpty))
      ableToDo   <- option(ableToDoGen)
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
      ableToDo         <- option(ableToDoGen)
      howEasy          <- option(howEasyQuestionGen)
      whyScore         <- option(arbitrary[String].suchThat(_.nonEmpty))
      howFeel          <- option(howDoYouFeelQuestionGen)
    } yield {
      BTAQuestions(mainService, mainServiceOther, ableToDo, howEasy, whyScore, howFeel)
    }

  lazy val trustsQuestionsGen: Gen[TrustsQuestions] =
    for {
      isAgent         <- option(yesNoGen)
      tryingToDo      <- option(tryingToDoQuestionGen)
      tryingToDoOther <- option(arbitrary[String].suchThat(_.nonEmpty))
      ableToDo        <- option(ableToDoGen)
      whyNotAbleToDo  <- option(arbitrary[String].suchThat(_.nonEmpty))
      howEasy         <- option(howEasyQuestionGen)
      whyScore        <- option(arbitrary[String].suchThat(_.nonEmpty))
      howFeel         <- option(howDoYouFeelQuestionGen)
    } yield {
      TrustsQuestions(isAgent, tryingToDo, tryingToDoOther, ableToDo, whyNotAbleToDo, howEasy, whyScore, howFeel)
    }

  lazy val pensionQuestionsGen: Gen[PensionQuestions] =
    for {
      ableToDo   <- option(ableToDoGen)
      howEasy    <- option(howEasyQuestionGen)
      whyScore   <- option(arbitrary[String].suchThat(_.nonEmpty))
      howFeel    <- option(howDoYouFeelQuestionGen)
      likelyToDo <- option(likelyToDoQuestionGen)
    } yield {
      PensionQuestions(ableToDo, howEasy, whyScore, howFeel, likelyToDo)
    }

  lazy val ccgQuestionsGen: Gen[CCGQuestions] =
    for {
      complianceCheckUnderstanding <- option(checkUnderstandingGen)
      treatedProfessionally        <- option(treatedProfessionallyGen)
      whyAnswer                    <- option(arbitrary[String].suchThat(_.nonEmpty))
      supportFutureTax             <- option(supportFutureGen)

    } yield
      CCGQuestions(
        complianceCheckUnderstanding,
        treatedProfessionally,
        whyAnswer,
        supportFutureTax
      )

  lazy val complaintFeedbackQuestionsGen: Gen[ComplaintFeedbackQuestions] =
    for {
      complaintHandledFairly <- option(yesNoGen)
      howEasy          <- option(howEasyQuestionGen)
      whyScore         <- option(arbitrary[String].suchThat(_.nonEmpty))
      howFeel          <- option(howDoYouFeelQuestionGen)
    } yield {
      ComplaintFeedbackQuestions(complaintHandledFairly, howEasy, whyScore, howFeel)
    }

  lazy val nmwCcgQuestionsGen: Gen[NmwCcgQuestions] =
    for {
      treatedProfessionally <- option(treatedProfessionallyGen)
      checkUnderstanding    <- option(checkUnderstandingGen)
      whyGiveAnswer         <- option(arbitrary[String].suchThat(_.nonEmpty))
      supportFutureNmw      <- option(supportFutureGen)

    } yield
      NmwCcgQuestions(
        treatedProfessionally,
        checkUnderstanding,
        whyGiveAnswer,
        supportFutureNmw
      )

  lazy val checkUnderstandingGen: Gen[CheckUnderstandingQuestion] =
    oneOf(CheckUnderstandingQuestion.values)

  lazy val treatedProfessionallyGen: Gen[TreatedProfessionallyQuestion] =
    oneOf(TreatedProfessionallyQuestion.values)

  lazy val supportFutureGen: Gen[SupportFutureQuestion] =
    oneOf(SupportFutureQuestion.values)

  lazy val howEasyQuestionGen: Gen[HowEasyQuestion] =
    oneOf(HowEasyQuestion.values)

  lazy val ableToDoGen: Gen[AbleToDo] =
    oneOf(AbleToDo.values)

  lazy val yesNoGen: Gen[YesNo] =
    oneOf(YesNo.values)

  lazy val howDoYouFeelQuestionGen: Gen[HowDoYouFeelQuestion] =
    oneOf(HowDoYouFeelQuestion.values)

  lazy val mainServiceQuestionGen: Gen[MainServiceQuestion] =
    oneOf(MainServiceQuestion.values)

  lazy val tryingToDoQuestionGen: Gen[TryingToDoQuestion] =
    oneOf(TryingToDoQuestion.values)

  lazy val likelyToDoQuestionGen: Gen[LikelyToDoQuestion] =
    oneOf(LikelyToDoQuestion.values)

  implicit lazy val arbitraryCheckUnderstandingQuestionSpec: Arbitrary[CheckUnderstandingQuestion] =
    Arbitrary(checkUnderstandingGen)

  implicit lazy val treatedProfessionallyQuestionSpec: Arbitrary[TreatedProfessionallyQuestion] =
    Arbitrary(treatedProfessionallyGen)

  implicit lazy val supportFutureQuestionSpec: Arbitrary[SupportFutureQuestion] =
    Arbitrary(supportFutureGen)
}
