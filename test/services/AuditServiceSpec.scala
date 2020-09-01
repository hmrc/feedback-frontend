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

package services

import controllers.EOTHOQuestionsController
import generators.ModelGenerators
import models._
import org.mockito.Matchers.{eq => eqTo, _}
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.test.UnitSpec
import org.scalacheck.Arbitrary._
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import utils.FeedbackFrontendHelper._

class AuditServiceSpec
    extends UnitSpec with MockitoSugar with BeforeAndAfter with ScalaCheckPropertyChecks with ModelGenerators
    with GuiceOneAppPerSuite {

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

  "generate correct payload for eat out to help out" in {

    forAll(arbitrary[FeedbackId], arbitrary[EOTHOQuestions]) { (feedbackId, questions) =>
      reset(auditConnector)

      auditService.eothoAudit(feedbackId, questions)

      val expected: Map[String, String] = Map(
        "origin"                     -> EOTHOQuestionsController.origin.value,
        "feedbackId"                 -> feedbackId.value,
        "numberOfEstablishments"     -> questions.numberOfEstablishments.fold("-")(_.toString),
        "numberOfEmployees"          -> questions.numberOfEmployees.fold("-")(_.toString),
        "whichRegions"               -> auditService.setToString(questions.whichRegions),
        "affectedJobs"               -> questions.affectedJobs.fold("-")(_.toString),
        "protectAtRiskJobs"          -> questions.protectAtRiskJobs.map(boolToString(_)).getOrElse("-"),
        "protectHospitalityIndustry" -> questions.protectHospitalityIndustry.map(boolToString(_)).getOrElse("-"),
        "comparedToMonTueWed"        -> questions.comparedToMonTueWed.fold("-")(_.toString),
        "comparedToThurFriSatSun"    -> questions.comparedToThurFriSatSun.fold("-")(_.toString),
        "comparedBusinessTurnover"   -> questions.comparedBusinessTurnover.fold("-")(_.toString),
        "furloughEmployees"          -> questions.furloughEmployees.fold("-")(_.toString),
        "businessFuturePlans"        -> questions.businessFuturePlans.fold("-")(_.toString),
        "offerDiscounts"             -> questions.offerDiscounts.fold("-")(_.toString)
      )

      verify(auditConnector, times(1))
        .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
    }
  }
}
