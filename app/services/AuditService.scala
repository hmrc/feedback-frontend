/*
 * Copyright 2018 HM Revenue & Customs
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

import javax.inject.Inject
import models.{BTAQuestions, OtherQuestions, PTAQuestions}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import utils.FeedbackFrontendHelper.boolToInt

import scala.concurrent.ExecutionContext


class AuditService @Inject()(auditConnector: AuditConnector) {

  def ptaAudit(origin:String, feedbackId: String, questions: PTAQuestions)(implicit hc: HeaderCarrier, ec: ExecutionContext): Unit = {

      val auditMap =
        Map(
          "origin"            -> origin,
          "feedbackId"        -> feedbackId,
          "neededToDo"        -> questions.neededToDo.getOrElse("-"),
          "ableToDo"          -> questions.ableToDo.map(boolToInt(_).toString).getOrElse("-"),
          "howEasyScore"      -> questions.howEasyScore.map(_.value.toString).getOrElse("-"),
          "whyGiveScore"      -> questions.whyGiveScore.getOrElse("-"),
          "howDoYouFeelScore" -> questions.howDoYouFeelScore.map(_.value.toString).getOrElse("-")
        )

    auditConnector.sendExplicitAudit("feedback", auditMap)
  }

  def btaAudit(origin:String, feedbackId: String, questions: BTAQuestions)(implicit hc: HeaderCarrier, ec: ExecutionContext): Unit = {

    val auditMap =
      Map(
        "origin"            -> origin,
        "feedbackId"        -> feedbackId,
        "mainService"       -> questions.mainService.map(_.toString).getOrElse("-"),
        "mainServiceOther"  -> questions.mainServiceOther.getOrElse("-"),
        "ableToDo"          -> questions.ableToDo.map(boolToInt(_).toString).getOrElse("-"),
        "howEasyScore"      -> questions.howEasyScore.map(_.value.toString).getOrElse("-"),
        "whyGiveScore"      -> questions.whyGiveScore.getOrElse("-"),
        "howDoYouFeelScore" -> questions.howDoYouFeelScore.map(_.value.toString).getOrElse("-")
      )

    auditConnector.sendExplicitAudit("feedback", auditMap)
  }

  def otherAudit(origin:String, feedbackId: String, questions: OtherQuestions)(implicit hc: HeaderCarrier, ec: ExecutionContext): Unit = {

    val auditMap =
      Map(
        "origin"            -> origin,
        "feedbackId"        -> feedbackId,
        "ableToDo"          -> questions.ableToDo.map(boolToInt(_).toString).getOrElse("-"),
        "howEasyScore"      -> questions.howEasyScore.map(_.value.toString).getOrElse("-"),
        "whyGiveScore"      -> questions.whyGiveScore.getOrElse("-"),
        "howDoYouFeelScore" -> questions.howDoYouFeelScore.map(_.value.toString).getOrElse("-")
      )

    auditConnector.sendExplicitAudit("feedback", auditMap)
  }
}
