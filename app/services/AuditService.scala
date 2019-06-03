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

import javax.inject.Inject
import models._
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import utils.FeedbackFrontendHelper.boolToInt

import scala.concurrent.ExecutionContext


class AuditService @Inject()(auditConnector: AuditConnector)(implicit ex: ExecutionContext) {

  private val auditType = "feedback"
  private val emptyMap: Map[String, String] = Map.empty


  type MapCont = Map[String, String] => Map[String, String]

  def withOrigin(origin: String): MapCont =
    _ + ("origin" -> origin)
  def withFeedbackId(feedbackId: String): MapCont =
    _ + ("feedbackId" -> feedbackId)
  def withNeededToDo(neededToDo: Option[String]): MapCont =
    _ + ("neededToDo" -> neededToDo.getOrElse("-"))
  def withAbleToDo(ableToDo: Option[Boolean]): MapCont =
    _ + ("ableToDo" -> ableToDo.map(boolToInt(_).toString).getOrElse("-"))
  def withHowEasyScore(howEasy: Option[HowEasyQuestion]): MapCont =
    _ + ("howEasyScore" -> howEasy.map(_.value.toString).getOrElse("-"))
  def withWhyGiveScore(whyScore: Option[String]): MapCont =
    _ + ("whyGiveScore" -> whyScore.getOrElse("-"))
  def withHowFeelScore(howFeel: Option[HowDoYouFeelQuestion]): MapCont =
    _ + ("howDoYouFeelScore" -> howFeel.map(_.value.toString).getOrElse("-"))
  def withMainService(mainService: Option[MainServiceQuestion]): MapCont =
    _ + ("mainService" -> mainService.map(_.toString).getOrElse("-"))
  def withMainServiceOther(mainServiceOther: Option[String]): MapCont =
    _ + ("mainServiceOther" -> mainServiceOther.getOrElse("-"))
  def withLikelyToDo(likelyToDo: Option[LikelyToDoQuestion]): MapCont =
    _ + ("likelyToDo" -> likelyToDo.map(_.toString).getOrElse("-"))
  def withGiveReason(giveReason: Option[GiveReason]): MapCont =
    _ + ("value" -> giveReason.map(_.toString).getOrElse("-"))
  def withOtherReason(otherReason: Option[String]): MapCont =
    _ + ("reason" -> otherReason.getOrElse("-"))
  def withGiveComments(answer: String): MapCont =
    _ + ("giveComments" -> answer)


  def ptaAudit(origin:String, feedbackId: String, questions: PTAQuestions)(implicit hc: HeaderCarrier): Unit = {

    val auditMap = (
      withOrigin(origin) andThen
      withFeedbackId(feedbackId) andThen
      withNeededToDo(questions.neededToDo) andThen
      withAbleToDo(questions.ableToDo) andThen
      withHowEasyScore(questions.howEasyScore) andThen
      withWhyGiveScore(questions.whyGiveScore) andThen
      withHowFeelScore(questions.howDoYouFeelScore)
    )(emptyMap)

    auditConnector.sendExplicitAudit(auditType, auditMap)
  }

  def btaAudit(origin:String, feedbackId: String, questions: BTAQuestions)(implicit hc: HeaderCarrier): Unit = {

    val auditMap = (
      withOrigin(origin) andThen
      withFeedbackId(feedbackId) andThen
      withMainService(questions.mainService) andThen
      withMainServiceOther(questions.mainServiceOther) andThen
      withAbleToDo(questions.ableToDo) andThen
      withHowEasyScore(questions.howEasyScore) andThen
      withWhyGiveScore(questions.whyGiveScore) andThen
      withHowFeelScore(questions.howDoYouFeelScore)
    )(emptyMap)

    auditConnector.sendExplicitAudit(auditType, auditMap)
  }

  def otherAudit(origin:String, feedbackId: String, questions: OtherQuestions)(implicit hc: HeaderCarrier): Unit = {

    val auditMap = (
      withOrigin(origin) andThen
      withFeedbackId(feedbackId) andThen
      withAbleToDo(questions.ableToDo) andThen
      withHowEasyScore(questions.howEasyScore) andThen
      withWhyGiveScore(questions.whyGiveScore) andThen
      withHowFeelScore(questions.howDoYouFeelScore)
    )(emptyMap)

    auditConnector.sendExplicitAudit(auditType, auditMap)
  }

  def pensionAudit(origin:String, feedbackId: String, questions: PensionQuestions)(implicit hc: HeaderCarrier): Unit = {

    val auditMap = (
      withOrigin(origin) andThen
      withFeedbackId(feedbackId) andThen
      withAbleToDo(questions.ableToDo) andThen
      withHowEasyScore(questions.howEasyScore) andThen
      withWhyGiveScore(questions.whyGiveScore) andThen
      withHowFeelScore(questions.howDoYouFeelScore) andThen
      withLikelyToDo(questions.likelyToDo)
    )(emptyMap)

    auditConnector.sendExplicitAudit(auditType, auditMap)
  }

  def giveReasonAudit(origin: String, feedbackId: String, questions: GiveReasonQuestions)(implicit hc: HeaderCarrier): Unit = {
    val auditMap = (
      withOrigin(origin) andThen
        withFeedbackId(feedbackId) andThen
        withGiveReason(questions.value) andThen
        withOtherReason(questions.reason)
      )(emptyMap)

    auditConnector.sendExplicitAudit(auditType, auditMap)
  }

  def giveCommentsAudit(origin: String, feedbackId: String, answer: String)(implicit hc: HeaderCarrier): Unit = {

    val auditMap = (
      withOrigin(origin) andThen
      withFeedbackId(feedbackId) andThen
      withGiveComments(answer)
    )(emptyMap)

    auditConnector.sendExplicitAudit(auditType, auditMap)
  }
}
