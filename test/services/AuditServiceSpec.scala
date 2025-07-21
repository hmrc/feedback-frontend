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

package services

import base.BaseSpec
import base.CommonSpecValues._
import models._
import models.ccg.{CheckUnderstandingQuestion, SupportFutureQuestion, TreatedProfessionallyQuestion}
import org.mockito.ArgumentMatchers.{eq => eqTo, _}
import org.mockito.Mockito._
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.FakeRequest
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector

import scala.concurrent.ExecutionContext
import scala.util.Random

class AuditServiceSpec extends BaseSpec with GuiceOneAppPerSuite {

  implicit val hc: HeaderCarrier    = HeaderCarrier()
  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  val auditConnector: AuditConnector = mock[AuditConnector]
  val auditService: AuditService     = new AuditService(auditConnector)(ec)

  "AuditService.ptaAudit" should {

    "generate correct payload for pta questions" in {

      for (serviceName <- serviceNames; stringFeedbackId <- feedbackIDs) {
        reset(auditConnector)

        val originService = Origin.fromString(serviceName)
        val feedbackId    = FeedbackId.fromSession(FakeRequest("GET", "").withSession("feedbackId" -> stringFeedbackId))

        val whatWasTheMainThingYouNeededToDoToday = Some("Sort out my life !")
        val wereYouAbleToDoWhatYouWant            = Some(AbleToDo.values(Random.nextInt(ableToDoQuestionNumberOfOptions)))
        val howEasyWasItToDoYourTask              = Some(HowEasyQuestion.values(Random.nextInt(howEasyQuestionNumberOfOptions)))
        val whyDidYouGiveThisScore                = Some("Because I felt like giving this score !")
        val howDoYouFeelAboutTheService           =
          Some(HowDoYouFeelQuestion.values(Random.nextInt(howDoYouFeelQuestionNumberOfOptions)))

        val answers = PTAQuestions(
          whatWasTheMainThingYouNeededToDoToday,
          wereYouAbleToDoWhatYouWant,
          howEasyWasItToDoYourTask,
          whyDidYouGiveThisScore,
          howDoYouFeelAboutTheService
        )

        auditService.ptaAudit(originService, feedbackId, answers)

        val expected = Map(
          "origin"            -> originService.value,
          "feedbackId"        -> feedbackId.value,
          "neededToDo"        -> answers.neededToDo.getOrElse("-"),
          "ableToDo"          -> answers.ableToDo.map(_.value.toString).getOrElse("-"),
          "howEasyScore"      -> answers.howEasyScore.map(_.value.toString).getOrElse("-"),
          "whyGiveScore"      -> answers.whyGiveScore.getOrElse("-"),
          "howDoYouFeelScore" -> answers.howDoYouFeelScore.map(_.value.toString).getOrElse("-")
        )

        verify(auditConnector, times(1))
          .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
      }
    }
  }

  "AuditService.btaAudit" should {

    "generate correct payload for bta questions" in {

      for (serviceName <- serviceNames; stringFeedbackId <- feedbackIDs) {
        reset(auditConnector)

        val originService = Origin.fromString(serviceName)
        val feedbackId    = FeedbackId.fromSession(FakeRequest("GET", "").withSession("feedbackId" -> stringFeedbackId))

        val whatWasTheMainServiceYouUsedToday =
          Some(MainServiceQuestion.values(Random.nextInt(mainServiceQuestionNumberOfOptions)))

        val otherOption                  = MainServiceQuestion.enumerable.withName("Other")
        val whatOtherServiceYouUsedToday =
          if (whatWasTheMainServiceYouUsedToday == otherOption) {
            Some("Personal Tax Allowance")
          } else {
            None
          }

        val wereYouAbleToDoWhatYouWant  = Some(AbleToDo.values(Random.nextInt(ableToDoQuestionNumberOfOptions)))
        val howEasyWasItToDoYourTask    = Some(HowEasyQuestion.values(Random.nextInt(howEasyQuestionNumberOfOptions)))
        val whyDidYouGiveThisScore      = Some("Because I felt like giving this score !")
        val howDoYouFeelAboutTheService =
          Some(HowDoYouFeelQuestion.values(Random.nextInt(howDoYouFeelQuestionNumberOfOptions)))

        val answers = BTAQuestions(
          whatWasTheMainServiceYouUsedToday,
          whatOtherServiceYouUsedToday,
          wereYouAbleToDoWhatYouWant,
          howEasyWasItToDoYourTask,
          whyDidYouGiveThisScore,
          howDoYouFeelAboutTheService
        )

        auditService.btaAudit(originService, feedbackId, answers)

        val expected = Map(
          "origin"            -> originService.value,
          "feedbackId"        -> feedbackId.value,
          "mainService"       -> answers.mainService.map(_.toString).getOrElse("-"),
          "mainServiceOther"  -> answers.mainServiceOther.getOrElse("-"),
          "ableToDo"          -> answers.ableToDo.map(_.value.toString).getOrElse("-"),
          "howEasyScore"      -> answers.howEasyScore.map(_.value.toString).getOrElse("-"),
          "whyGiveScore"      -> answers.whyGiveScore.getOrElse("-"),
          "howDoYouFeelScore" -> answers.howDoYouFeelScore.map(_.value.toString).getOrElse("-")
        )

        verify(auditConnector, times(1))
          .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
      }
    }
  }

  "AuditService.trustsAudit" should {

    "generate correct payload for trusts questions" in {

      val origin: Origin = Origin.fromString("trusts")

      for (serviceName <- serviceNames; stringFeedbackId <- feedbackIDs) {
        reset(auditConnector)

        val originService = Origin.fromString(serviceName)
        val feedbackId    = FeedbackId.fromSession(FakeRequest("GET", "").withSession("feedbackId" -> stringFeedbackId))

        val areYouAnAgent         = Some(YesNo.values(Random.nextInt(yesNoQuestionNumberOfOptions)))
        val whatWereYouTryingToDo = Some(TryingToDoQuestion.values(Random.nextInt(tryingToDoQuestionNumberOfOptions)))

        val otherOption                      = TryingToDoQuestion.enumerable.withName("Other")
        val whatOtherThingsWereYouTryingToDo =
          if (whatWereYouTryingToDo == otherOption) {
            Some("Various interesting stuff !")
          } else {
            None
          }

        val wereYouAbleToDoWhatYouWant       = Some(AbleToDo.values(Random.nextInt(ableToDoQuestionNumberOfOptions)))
        val whyWereYouNotAbleToDoWhatYouWant = Some("Due to various obstacles and scary challenges !!")
        val howEasyWasItToDoYourTask         = Some(HowEasyQuestion.values(Random.nextInt(howEasyQuestionNumberOfOptions)))
        val whyDidYouGiveThisScore           = Some("Because I felt like giving this score !")
        val howDoYouFeelAboutTheService      =
          Some(HowDoYouFeelQuestion.values(Random.nextInt(howDoYouFeelQuestionNumberOfOptions)))

        val answers = TrustsQuestions(
          areYouAnAgent,
          whatWereYouTryingToDo,
          whatOtherThingsWereYouTryingToDo,
          wereYouAbleToDoWhatYouWant,
          whyWereYouNotAbleToDoWhatYouWant,
          howEasyWasItToDoYourTask,
          whyDidYouGiveThisScore,
          howDoYouFeelAboutTheService
        )

        auditService.trustsAudit(originService, feedbackId, answers)

        val expected = Map(
          "origin"            -> originService.value,
          "feedbackId"        -> feedbackId.value,
          "isAgent"           -> answers.isAgent.map(_.value.toString).getOrElse("-"),
          "tryingToDo"        -> answers.tryingToDo.map(_.toString).getOrElse("-"),
          "tryingToDoOther"   -> answers.tryingToDoOther.getOrElse("-"),
          "ableToDo"          -> answers.ableToDo.map(_.value.toString).getOrElse("-"),
          "whyNotAbleToDo"    -> answers.whyNotAbleToDo.getOrElse("-"),
          "howEasyScore"      -> answers.howEasyScore.map(_.value.toString).getOrElse("-"),
          "whyGiveScore"      -> answers.whyGiveScore.getOrElse("-"),
          "howDoYouFeelScore" -> answers.howDoYouFeelScore.map(_.value.toString).getOrElse("-")
        )

        verify(auditConnector, times(1))
          .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
      }
    }
  }

  "generate correct payload for other questions" in {

    for (serviceName <- serviceNames; stringFeedbackId <- feedbackIDs; stringClientId <- clientIDs) {
      reset(auditConnector)

      val originService = Origin.fromString(serviceName)
      val feedbackId    = FeedbackId.fromSession(FakeRequest("GET", "").withSession("feedbackId" -> stringFeedbackId))

      val wereYouAbleToDoWhatYouWant  = Some(AbleToDo.values(Random.nextInt(ableToDoQuestionNumberOfOptions)))
      val howEasyWasItToDoYourTask    = Some(HowEasyQuestion.values(Random.nextInt(howEasyQuestionNumberOfOptions)))
      val whyDidYouGiveThisScore      = Some("Because I felt like giving this score !")
      val howDoYouFeelAboutTheService =
        Some(HowDoYouFeelQuestion.values(Random.nextInt(howDoYouFeelQuestionNumberOfOptions)))

      val answers = OtherQuestions(
        wereYouAbleToDoWhatYouWant,
        howEasyWasItToDoYourTask,
        whyDidYouGiveThisScore,
        howDoYouFeelAboutTheService
      )

      val clientId =
        Cid.fromUrl(FakeRequest("GET", "").withHeaders("referer" -> s"/feedback/EXAMPLE?cid=$stringClientId"))

      auditService.otherAudit(originService, feedbackId, answers, clientId)

      val expected = Map(
        "origin"            -> originService.value,
        "feedbackId"        -> feedbackId.value,
        "ableToDo"          -> answers.ableToDo.map(_.value.toString).getOrElse("-"),
        "howEasyScore"      -> answers.howEasyScore.map(_.value.toString).getOrElse("-"),
        "whyGiveScore"      -> answers.whyGiveScore.getOrElse("-"),
        "howDoYouFeelScore" -> answers.howDoYouFeelScore.map(_.value.toString).getOrElse("-"),
        "cid"               -> clientId.value
      )

      verify(auditConnector, times(1))
        .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
    }
  }

  "generate correct payload for give reasons" in {

    for (serviceName <- serviceNames; stringFeedbackId <- feedbackIDs) {
      reset(auditConnector)

      val originService = Origin.fromString(serviceName)
      val feedbackId    = FeedbackId.fromSession(FakeRequest("GET", "").withSession("feedbackId" -> stringFeedbackId))

      val whatDidYouComeToDo     = Some(GiveReason.values(Random.nextInt(giveReasonNumberOfOptions)))
      val otherOption            = GiveReason.enumerable.withName("Other")
      val whatAreTheOtherReasons =
        if (whatDidYouComeToDo == otherOption) {
          Some("Various interesting reasons !")
        } else {
          None
        }

      val answers = GiveReasonQuestions(
        whatDidYouComeToDo,
        whatAreTheOtherReasons
      )

      auditService.giveReasonAudit(originService, feedbackId, answers)

      val expected = Map(
        "origin"     -> originService.value,
        "feedbackId" -> feedbackId.value,
        "value"      -> answers.value.fold("-")(_.toString),
        "reason"     -> answers.reason.getOrElse("-")
      )

      verify(auditConnector, times(1))
        .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
    }
  }

  "AuditService.ccgAudit" should {

    "generate correct payload for ccg questions" in {

      for (serviceName <- serviceNames; stringFeedbackId <- feedbackIDs; stringClientId <- clientIDs) {
        reset(auditConnector)

        val originService = Origin.fromString(serviceName)
        val feedbackId    = FeedbackId.fromSession(FakeRequest("GET", "").withSession("feedbackId" -> stringFeedbackId))

        val haveYouBeenTreatedProfessionally                  =
          Some(TreatedProfessionallyQuestion.values(Random.nextInt(treatedProfessionallyQuestionNumberOfOptions)))
        val howEasyWasItToUnderstandWhatWasHappening          =
          Some(CheckUnderstandingQuestion.values(Random.nextInt(checkUnderstandingQuestionNumberOfOptions)))
        val whyDidYouGiveThisScore                            = Some("Because I felt like giving this score !")
        val willYourHMRCInteractionsHelpYouMeetTaxObligations =
          Some(SupportFutureQuestion.values(Random.nextInt(supportFutureQuestionNumberOfOptions)))

        val answers = CCGQuestions(
          howEasyWasItToUnderstandWhatWasHappening,
          haveYouBeenTreatedProfessionally,
          whyDidYouGiveThisScore,
          willYourHMRCInteractionsHelpYouMeetTaxObligations
        )

        val clientId =
          Cid.fromUrl(FakeRequest("GET", "").withHeaders("referer" -> s"/feedback/EXAMPLE?cid=$stringClientId"))

        auditService.ccgAudit(originService, feedbackId, answers, clientId)

        val expected = Map(
          "origin"                -> originService.value,
          "feedbackId"            -> feedbackId.value,
          "checkUnderstanding"    -> answers.complianceCheckUnderstanding.map(_.toString).getOrElse("-"),
          "treatedProfessionally" -> answers.treatedProfessionally.map(_.toString).getOrElse("-"),
          "whyGiveAnswer"         -> answers.whyGiveAnswer.getOrElse("-"),
          "supportFuture"         -> answers.supportFutureTaxQuestion.map(_.toString).getOrElse("-"),
          "cid"                   -> clientId.value
        )

        verify(auditConnector, times(1))
          .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
      }
    }
  }

  "AuditService.nmwCcgAudit" should {

    "generate correct payload for nmw questions" in {

      for (serviceName <- serviceNames; stringFeedbackId <- feedbackIDs) {
        reset(auditConnector)

        val originService = Origin.fromString(serviceName)
        val feedbackId    = FeedbackId.fromSession(FakeRequest("GET", "").withSession("feedbackId" -> stringFeedbackId))

        val haveYouBeenTreatedProfessionally                  =
          Some(TreatedProfessionallyQuestion.values(Random.nextInt(treatedProfessionallyQuestionNumberOfOptions)))
        val howEasyWasItToUnderstandWhatWasHappening          =
          Some(CheckUnderstandingQuestion.values(Random.nextInt(checkUnderstandingQuestionNumberOfOptions)))
        val whyDidYouGiveThisAnswer                           = Some("Because I felt like giving this answer !")
        val willYourHMRCInteractionsHelpYouMeetTaxObligations =
          Some(SupportFutureQuestion.values(Random.nextInt(supportFutureQuestionNumberOfOptions)))

        val answers = NmwCcgQuestions(
          haveYouBeenTreatedProfessionally,
          howEasyWasItToUnderstandWhatWasHappening,
          whyDidYouGiveThisAnswer,
          willYourHMRCInteractionsHelpYouMeetTaxObligations
        )

        auditService.nmwCcgAudit(originService, feedbackId, answers)

        val expected = Map(
          "origin"                -> originService.value,
          "feedbackId"            -> feedbackId.value,
          "treatedProfessionally" -> answers.treatedProfessionally.map(_.toString).getOrElse("-"),
          "checkUnderstanding"    -> answers.checkUnderstanding.map(_.toString).getOrElse("-"),
          "whyGiveAnswer"         -> answers.whyGiveAnswer.getOrElse("-"),
          "supportFuture"         -> answers.supportFutureNmw.map(_.toString).getOrElse("-")
        )

        verify(auditConnector, times(1))
          .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
      }
    }
  }

  "AuditService.pensionAudit" should {

    "generate correct payload for pension questions" in {

      for (serviceName <- serviceNames; stringFeedbackId <- feedbackIDs) {
        reset(auditConnector)

        val originService = Origin.fromString(serviceName)
        val feedbackId    = FeedbackId.fromSession(FakeRequest("GET", "").withSession("feedbackId" -> stringFeedbackId))

        val wereYouAbleToDoWhatYouWant                 = Some(AbleToDo.values(Random.nextInt(ableToDoQuestionNumberOfOptions)))
        val howEasyWasItToDoYourTask                   = Some(HowEasyQuestion.values(Random.nextInt(howEasyQuestionNumberOfOptions)))
        val whyDidYouGiveThisScore                     = Some("Because I felt like giving this score !")
        val howDoYouFeelAboutTheService                =
          Some(HowDoYouFeelQuestion.values(Random.nextInt(howDoYouFeelQuestionNumberOfOptions)))
        val whatWillYouDoAfterCheckingYourStatePension =
          Some(LikelyToDoQuestion.values(Random.nextInt(likelyToDoQuestionNumberOfOptions)))

        val answers = PensionQuestions(
          wereYouAbleToDoWhatYouWant,
          howEasyWasItToDoYourTask,
          whyDidYouGiveThisScore,
          howDoYouFeelAboutTheService,
          whatWillYouDoAfterCheckingYourStatePension
        )

        auditService.pensionAudit(originService, feedbackId, answers)

        val expected = Map(
          "origin"            -> originService.value,
          "feedbackId"        -> feedbackId.value,
          "ableToDo"          -> answers.ableToDo.map(_.value.toString).getOrElse("-"),
          "howEasyScore"      -> answers.howEasyScore.map(_.value.toString).getOrElse("-"),
          "whyGiveScore"      -> answers.whyGiveScore.getOrElse("-"),
          "howDoYouFeelScore" -> answers.howDoYouFeelScore.map(_.value.toString).getOrElse("-"),
          "likelyToDo"        -> answers.likelyToDo.map(_.toString).getOrElse("-")
        )

        verify(auditConnector, times(1))
          .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
      }
    }
  }

  "AuditService.giveCommentsAudit" should {

    "generate correct payload for give comments questions" in {

      for (serviceName <- serviceNames; stringFeedbackId <- feedbackIDs) {
        reset(auditConnector)

        val originService = Origin.fromString(serviceName)
        val feedbackId    = FeedbackId.fromSession(FakeRequest("GET", "").withSession("feedbackId" -> stringFeedbackId))
        val comments      = "I have so much to say so here goes: blah blah blah blah !!"

        auditService.giveCommentsAudit(originService, feedbackId, comments)

        val expected = Map(
          "origin"       -> originService.value,
          "feedbackId"   -> feedbackId.value,
          "giveComments" -> comments
        )

        verify(auditConnector, times(1))
          .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
      }
    }
  }
  "AuditService.complaintFeedbackAudit" should {

    "generate correct payload for complaintFeedback questions" in {

      for (serviceName <- serviceNames; stringFeedbackId <- feedbackIDs; stringClientId <- clientIDs) {
        reset(auditConnector)

        val originService = Origin.fromString(serviceName)
        val feedbackId    = FeedbackId.fromSession(FakeRequest("GET", "").withSession("feedbackId" -> stringFeedbackId))

        val didHmrcHandleYourComplaintFairly = Some(YesNo.values(Random.nextInt(yesNoQuestionNumberOfOptions)))
        val howEasyWasItToDoYourTask         = Some(HowEasyQuestion.values(Random.nextInt(howEasyQuestionNumberOfOptions)))
        val whyDidYouGiveThisScore           = Some("Because I felt like giving this score !")
        val howDoYouFeelAboutTheService      =
          Some(HowDoYouFeelQuestion.values(Random.nextInt(howDoYouFeelQuestionNumberOfOptions)))

        val answers = ComplaintFeedbackQuestions(
          didHmrcHandleYourComplaintFairly,
          howEasyWasItToDoYourTask,
          whyDidYouGiveThisScore,
          howDoYouFeelAboutTheService
        )

        val clientId =
          Cid.fromUrl(FakeRequest("GET", "").withHeaders("referer" -> s"/feedback/EXAMPLE?cid=$stringClientId"))

        auditService.complaintFeedbackAudit(originService, feedbackId, answers, clientId)

        val expected = Map(
          "origin"                 -> originService.value,
          "feedbackId"             -> feedbackId.value,
          "complaintHandledFairly" -> answers.complaintHandledFairly.map(_.value.toString).getOrElse("-"),
          "howEasyScore"           -> answers.howEasyScore.map(_.value.toString).getOrElse("-"),
          "whyGiveScore"           -> answers.whyGiveScore.getOrElse("-"),
          "howDoYouFeelScore"      -> answers.howDoYouFeelScore.map(_.value.toString).getOrElse("-"),
          "cid"                    -> clientId.value
        )

        verify(auditConnector, times(1))
          .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
      }
    }
  }
}
