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

package services

import base.BaseSpec
import models._
import org.mockito.Matchers.{eq => eqTo, _}
import org.mockito.Mockito._
import org.scalacheck.Arbitrary._
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector

class AuditServiceSpec extends BaseSpec with GuiceOneAppPerSuite {

  implicit val hc = HeaderCarrier()
  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global

  val auditConnector = mock[AuditConnector]
  val auditService = new AuditService(auditConnector)(ec)

  "AuditService.ptaAudit" should {

    "generate correct payload for pta questions" in {

      forAll(arbitrary[Origin], arbitrary[FeedbackId], arbitrary[PTAQuestions]) { (origin, feedbackId, questions) =>
        reset(auditConnector)

        auditService.ptaAudit(origin, feedbackId, questions)

        val expected = Map(
          "origin"            -> origin.value,
          "feedbackId"        -> feedbackId.value,
          "neededToDo"        -> questions.neededToDo.getOrElse("-"),
          "ableToDo"          -> questions.ableToDo.map(_.value.toString).getOrElse("-"),
          "howEasyScore"      -> questions.howEasyScore.map(_.value.toString).getOrElse("-"),
          "whyGiveScore"      -> questions.whyGiveScore.getOrElse("-"),
          "howDoYouFeelScore" -> questions.howDoYouFeelScore.map(_.value.toString).getOrElse("-")
        )

        verify(auditConnector, times(1))
          .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
      }
    }
  }

  "AuditService.btaAudit" should {

    "generate correct payload for bta questions" in {

      forAll(arbitrary[Origin], arbitrary[FeedbackId], arbitrary[BTAQuestions]) { (origin, feedbackId, questions) =>
        reset(auditConnector)

        auditService.btaAudit(origin, feedbackId, questions)

        val expected = Map(
          "origin"            -> origin.value,
          "feedbackId"        -> feedbackId.value,
          "mainService"       -> questions.mainService.map(_.toString).getOrElse("-"),
          "mainServiceOther"  -> questions.mainServiceOther.getOrElse("-"),
          "ableToDo"          -> questions.ableToDo.map(_.value.toString).getOrElse("-"),
          "howEasyScore"      -> questions.howEasyScore.map(_.value.toString).getOrElse("-"),
          "whyGiveScore"      -> questions.whyGiveScore.getOrElse("-"),
          "howDoYouFeelScore" -> questions.howDoYouFeelScore.map(_.value.toString).getOrElse("-")
        )

        verify(auditConnector, times(1))
          .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
      }
    }
  }

  "AuditService.trustsAudit" should {

    "generate correct payload for trusts questions" in {

      val origin: Origin = Origin.fromString("trusts")

      forAll(arbitrary[FeedbackId], arbitrary[TrustsQuestions]) { (feedbackId, questions) =>
        reset(auditConnector)

        auditService.trustsAudit(origin, feedbackId, questions)

        val expected = Map(
          "origin"            -> origin.value,
          "feedbackId"        -> feedbackId.value,
          "isAgent"           -> questions.isAgent.map(_.value.toString).getOrElse("-"),
          "tryingToDo"        -> questions.tryingToDo.map(_.toString).getOrElse("-"),
          "tryingToDoOther"   -> questions.tryingToDoOther.getOrElse("-"),
          "ableToDo"          -> questions.ableToDo.map(_.value.toString).getOrElse("-"),
          "whyNotAbleToDo"    -> questions.whyNotAbleToDo.getOrElse("-"),
          "howEasyScore"      -> questions.howEasyScore.map(_.value.toString).getOrElse("-"),
          "whyGiveScore"      -> questions.whyGiveScore.getOrElse("-"),
          "howDoYouFeelScore" -> questions.howDoYouFeelScore.map(_.value.toString).getOrElse("-")
        )

        verify(auditConnector, times(1))
          .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
      }
    }
  }

  "generate correct payload for other questions" in {

    forAll(arbitrary[Origin], arbitrary[FeedbackId], arbitrary[OtherQuestions], arbitrary[Cid]) {
      (origin, feedbackId, questions, cid) =>
        reset(auditConnector)

        auditService.otherAudit(origin, feedbackId, questions, cid)

        val expected = Map(
          "origin"            -> origin.value,
          "feedbackId"        -> feedbackId.value,
          "ableToDo"          -> questions.ableToDo.map(_.value.toString).getOrElse("-"),
          "howEasyScore"      -> questions.howEasyScore.map(_.value.toString).getOrElse("-"),
          "whyGiveScore"      -> questions.whyGiveScore.getOrElse("-"),
          "howDoYouFeelScore" -> questions.howDoYouFeelScore.map(_.value.toString).getOrElse("-"),
          "cid"               -> cid.value
        )


        verify(auditConnector, times(1))
          .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
    }
  }

  "generate correct payload for give reasons" in {

    forAll(arbitrary[Origin], arbitrary[FeedbackId], arbitrary[GiveReasonQuestions]) {
      (origin, feedbackId, questions) =>
        reset(auditConnector)

        auditService.giveReasonAudit(origin, feedbackId, questions)

        val expected = Map(
          "origin"     -> origin.value,
          "feedbackId" -> feedbackId.value,
          "value"      -> questions.value.fold("-")(_.toString),
          "reason"     -> questions.reason.getOrElse("-")
        )

        verify(auditConnector, times(1))
          .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
    }
  }

  "AuditService.ccgAudit" should {

    "generate correct payload for ccg questions" in {

      forAll(arbitrary[Origin], arbitrary[FeedbackId], arbitrary[CCGQuestions], arbitrary[Cid]) {
        (origin, feedbackId, questions, cid) =>
          reset(auditConnector)

          auditService.ccgAudit(origin, feedbackId, questions, cid)

          val expected = Map(
            "origin"                -> origin.value,
            "feedbackId"            -> feedbackId.value,
            "checkUnderstanding"    -> questions.complianceCheckUnderstanding.map(_.toString).getOrElse("-"),
            "treatedProfessionally" -> questions.treatedProfessionally.map(_.toString).getOrElse("-"),
            "whyGiveAnswer"         -> questions.whyGiveAnswer.getOrElse("-"),
            "supportFuture"         -> questions.supportFutureTaxQuestion.map(_.toString).getOrElse("-"),
            "cid"                   -> cid.value
          )

          verify(auditConnector, times(1))
            .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
      }
    }
  }

  "AuditService.nmwCcgAudit" should {

    "generate correct payload for nmw questions" in {

      forAll(arbitrary[Origin], arbitrary[FeedbackId], arbitrary[NmwCcgQuestions]) { (origin, feedbackId, questions) =>
        reset(auditConnector)

        auditService.nmwCcgAudit(origin, feedbackId, questions)

        val expected = Map(
          "origin"                -> origin.value,
          "feedbackId"            -> feedbackId.value,
          "treatedProfessionally" -> questions.treatedProfessionally.map(_.toString).getOrElse("-"),
          "checkUnderstanding"    -> questions.checkUnderstanding.map(_.toString).getOrElse("-"),
          "whyGiveAnswer"         -> questions.whyGiveAnswer.getOrElse("-"),
          "supportFuture"         -> questions.supportFutureNmw.map(_.toString).getOrElse("-")
        )

        verify(auditConnector, times(1))
          .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
      }
    }
  }

  "AuditService.pensionAudit" should {

    "generate correct payload for pension questions" in {

      forAll(arbitrary[Origin], arbitrary[FeedbackId], arbitrary[PensionQuestions]) { (origin, feedbackId, questions) =>
        reset(auditConnector)

        auditService.pensionAudit(origin, feedbackId, questions)

        val expected = Map(
          "origin"            -> origin.value,
          "feedbackId"        -> feedbackId.value,
          "ableToDo"          -> questions.ableToDo.map(_.value.toString).getOrElse("-"),
          "howEasyScore"      -> questions.howEasyScore.map(_.value.toString).getOrElse("-"),
          "whyGiveScore"      -> questions.whyGiveScore.getOrElse("-"),
          "howDoYouFeelScore" -> questions.howDoYouFeelScore.map(_.value.toString).getOrElse("-"),
          "likelyToDo"        -> questions.likelyToDo.map(_.toString).getOrElse("-")
        )

        verify(auditConnector, times(1))
          .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
      }
    }
  }

  "AuditService.giveCommentsAudit" should {

    "generate correct payload for give comments questions" in {

      forAll(arbitrary[Origin], arbitrary[FeedbackId], arbitrary[String]) { (origin, feedbackId, comments) =>
        reset(auditConnector)

        auditService.giveCommentsAudit(origin, feedbackId, comments)

        val expected = Map(
          "origin"       -> origin.value,
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

      forAll(arbitrary[Origin], arbitrary[FeedbackId], arbitrary[ComplaintFeedbackQuestions], arbitrary[Cid]) {
        (origin, feedbackId, questions, cid) =>
          reset(auditConnector)

          auditService.complaintFeedbackAudit(origin, feedbackId, questions, cid)

          val expected = Map(
            "origin" -> origin.value,
            "feedbackId" -> feedbackId.value,
            "complaintHandledFairly" -> questions.complaintHandledFairly.map(_.value.toString).getOrElse("-"),
            "howEasyScore" -> questions.howEasyScore.map(_.value.toString).getOrElse("-"),
            "whyGiveScore" -> questions.whyGiveScore.getOrElse("-"),
            "howDoYouFeelScore" -> questions.howDoYouFeelScore.map(_.value.toString).getOrElse("-"),
            "cid" -> cid.value
          )

          verify(auditConnector, times(1))
            .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
      }
    }
  }
}
