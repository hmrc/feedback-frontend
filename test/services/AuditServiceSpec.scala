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

package services

import generators.ModelGenerators
import models._
import org.mockito.Matchers.{eq => eqTo, _}
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter
import org.scalatest.mockito.MockitoSugar
import org.scalatest.prop.PropertyChecks
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.test.UnitSpec
import org.scalacheck.Arbitrary._
import utils.FeedbackFrontendHelper.boolToInt

import scala.concurrent.ExecutionContext

class AuditServiceSpec extends UnitSpec with MockitoSugar with BeforeAndAfter with PropertyChecks with ModelGenerators {

  implicit val hc = HeaderCarrier()
  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global

  val auditConnector = mock[AuditConnector]
  val auditService = new AuditService(auditConnector)(ec)

  "AuditService.ptaAudit" should {

    "generate correct payload for pta questions" in {

      forAll(arbitrary[String], arbitrary[String], arbitrary[PTAQuestions]) {
        (origin, feedbackId, questions) =>
          reset(auditConnector)

          auditService.ptaAudit(origin, feedbackId, questions)

          val expected = Map("origin" -> origin,
            "feedbackId"        -> feedbackId,
            "neededToDo"        -> questions.neededToDo.getOrElse("-"),
            "ableToDo"          -> questions.ableToDo.map(boolToInt(_).toString).getOrElse("-"),
            "howEasyScore"      -> questions.howEasyScore.map(_.value.toString).getOrElse("-"),
            "whyGiveScore"      -> questions.whyGiveScore.getOrElse("-"),
            "howDoYouFeelScore" -> questions.howDoYouFeelScore.map(_.value.toString).getOrElse("-"))

          verify(auditConnector, times(1))
            .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
      }
    }
  }

  "AuditService.btaAudit" should {

    "generate correct payload for bta questions" in {

      forAll(arbitrary[String], arbitrary[String], arbitrary[BTAQuestions]) {
        (origin, feedbackId, questions) =>
          reset(auditConnector)

          auditService.btaAudit(origin, feedbackId, questions)

          val expected = Map("origin" -> origin,
            "feedbackId"        -> feedbackId,
            "mainService"       -> questions.mainService.map(_.toString).getOrElse("-"),
            "mainServiceOther"  -> questions.mainServiceOther.getOrElse("-"),
            "ableToDo"          -> questions.ableToDo.map(boolToInt(_).toString).getOrElse("-"),
            "howEasyScore"      -> questions.howEasyScore.map(_.value.toString).getOrElse("-"),
            "whyGiveScore"      -> questions.whyGiveScore.getOrElse("-"),
            "howDoYouFeelScore" -> questions.howDoYouFeelScore.map(_.value.toString).getOrElse("-"))

          verify(auditConnector, times(1))
            .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
      }
    }
  }

  "generate correct payload for other questions" in {

    forAll(arbitrary[String], arbitrary[String], arbitrary[OtherQuestions]) {
      (origin, feedbackId, questions) =>
        reset(auditConnector)

        auditService.otherAudit(origin, feedbackId, questions)

        val expected = Map("origin" -> origin,
          "feedbackId"        -> feedbackId,
          "ableToDo"          -> questions.ableToDo.map(boolToInt(_).toString).getOrElse("-"),
          "howEasyScore"      -> questions.howEasyScore.map(_.value.toString).getOrElse("-"),
          "whyGiveScore"      -> questions.whyGiveScore.getOrElse("-"),
          "howDoYouFeelScore" -> questions.howDoYouFeelScore.map(_.value.toString).getOrElse("-"))

        verify(auditConnector, times(1))
          .sendExplicitAudit(eqTo("feedback"), eqTo(expected))(any(), any())
    }
  }
}
