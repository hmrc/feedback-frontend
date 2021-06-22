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

package services

import generators.ModelGenerators
import models._
import org.mockito.Matchers.{eq => eqTo, _}
import org.mockito.Mockito._
import org.scalacheck.Arbitrary._
import org.scalatest.{BeforeAndAfter, Matchers, OptionValues, WordSpec}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import utils.FeedbackFrontendHelper._

class AuditServiceSpec
    extends WordSpec with Matchers with OptionValues with MockitoSugar with BeforeAndAfter with ScalaCheckPropertyChecks
    with ModelGenerators with GuiceOneAppPerSuite {

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
          "ableToDo"          -> questions.ableToDo.map(boolToInt(_).toString).getOrElse("-"),
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
          "ableToDo"          -> questions.ableToDo.map(boolToInt(_).toString).getOrElse("-"),
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

    "generate correct payload for bta questions" in {

      forAll(arbitrary[Origin], arbitrary[FeedbackId], arbitrary[TrustsQuestions]) { (origin, feedbackId, questions) =>
        reset(auditConnector)

        auditService.trustsAudit(origin, feedbackId, questions)

        val expected = Map(
          "origin"            -> origin.value,
          "feedbackId"        -> feedbackId.value,
          "isAgent"           -> questions.isAgent.map(boolToInt(_).toString).getOrElse("-"),
          "tryingToDo"        -> questions.tryingToDo.map(_.toString).getOrElse("-"),
          "tryingToDoOther"   -> questions.tryingToDoOther.getOrElse("-"),
          "ableToDo"          -> questions.ableToDo.map(boolToInt(_).toString).getOrElse("-"),
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

    forAll(arbitrary[Origin], arbitrary[FeedbackId], arbitrary[OtherQuestions]) { (origin, feedbackId, questions) =>
      reset(auditConnector)

      auditService.otherAudit(origin, feedbackId, questions)

      val expected = Map(
        "origin"            -> origin.value,
        "feedbackId"        -> feedbackId.value,
        "ableToDo"          -> questions.ableToDo.map(boolToInt(_).toString).getOrElse("-"),
        "howEasyScore"      -> questions.howEasyScore.map(_.value.toString).getOrElse("-"),
        "whyGiveScore"      -> questions.whyGiveScore.getOrElse("-"),
        "howDoYouFeelScore" -> questions.howDoYouFeelScore.map(_.value.toString).getOrElse("-")
      )

      verify(auditConnector, times(1))
        .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
    }
  }

  "generate correct payload for otherQuestionsEmployeeExpensesBeta questions" in {

    forAll(arbitrary[Origin], arbitrary[FeedbackId], arbitrary[OtherQuestionsEmployeeExpensesBeta]) {
      (origin, feedbackId, questions) =>
        reset(auditConnector)

        auditService.otherEmployeeExpensesBetaAudit(origin, feedbackId, questions)

        val expected = Map(
          "origin"            -> origin.value,
          "feedbackId"        -> feedbackId.value,
          "ableToDo"          -> questions.ableToDo.map(boolToInt(_).toString).getOrElse("-"),
          "howEasyScore"      -> questions.howEasyScore.map(_.value.toString).getOrElse("-"),
          "whyGiveScore"      -> questions.whyGiveScore.getOrElse("-"),
          "howDoYouFeelScore" -> questions.howDoYouFeelScore.map(_.value.toString).getOrElse("-"),
          "fullName"          -> questions.fullName.getOrElse("-"),
          "email"             -> questions.email.getOrElse("-")
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

      forAll(arbitrary[Origin], arbitrary[FeedbackId], arbitrary[CCGQuestions]) { (origin, feedbackId, questions) =>
        reset(auditConnector)

        auditService.ccgAudit(origin, feedbackId, questions)

        val expected = Map(
          "origin"                       -> origin.value,
          "feedbackId"                   -> feedbackId.value,
          "complianceCheckUnderstanding" -> questions.complianceCheckUnderstanding.map(_.toString).getOrElse("-"),
          "treatedProfessionally"        -> questions.treatedProfessionally.map(_.toString).getOrElse("-"),
          "whyGiveAnswer"                -> questions.whyGiveAnswer.getOrElse("-"),
          "supportFutureTax"             -> questions.supportFutureTaxQuestion.map(_.toString).getOrElse("-")
        )

        verify(auditConnector, times(1))
          .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
      }
    }
  }
}
