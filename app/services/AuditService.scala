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
import javax.inject.Inject
import models._
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import utils.FeedbackFrontendHelper._

import scala.concurrent.ExecutionContext

class AuditService @Inject()(auditConnector: AuditConnector)(implicit ex: ExecutionContext) {

  private val auditType = "feedback"
  private val emptyMap: Map[String, String] = Map.empty

  type MapCont = Map[String, String] => Map[String, String]

  def withOrigin(origin: Origin): MapCont =
    _ + ("origin" -> origin.value)
  def withFeedbackId(feedbackId: FeedbackId): MapCont =
    _ + ("feedbackId" -> feedbackId.value)
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
  def withFullName(fullName: Option[String]): MapCont =
    _ + ("fullName" -> fullName.getOrElse(("-")))
  def withEmail(email: Option[String]): MapCont =
    _ + ("email" -> email.getOrElse("-"))

  def ptaAudit(origin: Origin, feedbackId: FeedbackId, questions: PTAQuestions)(implicit hc: HeaderCarrier): Unit = {

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

  def btaAudit(origin: Origin, feedbackId: FeedbackId, questions: BTAQuestions)(implicit hc: HeaderCarrier): Unit = {

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

  def otherAudit(origin: Origin, feedbackId: FeedbackId, questions: OtherQuestions)(
    implicit hc: HeaderCarrier): Unit = {

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

  def otherEmployeeExpensesBetaAudit(
    origin: Origin,
    feedbackId: FeedbackId,
    questions: OtherQuestionsEmployeeExpensesBeta)(implicit hc: HeaderCarrier): Unit = {

    val auditMap = (
      withOrigin(origin) andThen
        withFeedbackId(feedbackId) andThen
        withAbleToDo(questions.ableToDo) andThen
        withHowEasyScore(questions.howEasyScore) andThen
        withWhyGiveScore(questions.whyGiveScore) andThen
        withHowFeelScore(questions.howDoYouFeelScore) andThen
        withFullName(questions.fullName) andThen
        withEmail(questions.email)
    )(emptyMap)
    auditConnector.sendExplicitAudit(auditType, auditMap)
  }

  def pensionAudit(origin: Origin, feedbackId: FeedbackId, questions: PensionQuestions)(
    implicit hc: HeaderCarrier): Unit = {

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

  def giveReasonAudit(origin: Origin, feedbackId: FeedbackId, questions: GiveReasonQuestions)(
    implicit hc: HeaderCarrier): Unit = {
    val auditMap = (
      withOrigin(origin) andThen
        withFeedbackId(feedbackId) andThen
        withGiveReason(questions.value) andThen
        withOtherReason(questions.reason)
    )(emptyMap)

    auditConnector.sendExplicitAudit(auditType, auditMap)
  }

  def giveCommentsAudit(origin: Origin, feedbackId: FeedbackId, answer: String)(implicit hc: HeaderCarrier): Unit = {

    val auditMap = (
      withOrigin(origin) andThen
        withFeedbackId(feedbackId) andThen
        withGiveComments(answer)
    )(emptyMap)

    auditConnector.sendExplicitAudit(auditType, auditMap)
  }

  def eothoAudit(feedbackId: FeedbackId, questions: EOTHOQuestions)(implicit hc: HeaderCarrier): Unit = {
    val detail = EothoAuditEvent(EOTHOQuestionsController.origin.value, feedbackId.value, questions)
    auditConnector.sendExplicitAudit(auditType, detail)
  }

  def feedbackSurveyAbleToDoAudit(origin: String, ableToDoWhatNeeded: Option[String])(
    implicit hc: HeaderCarrier): Unit = {

    val auditMap = Map("origin" -> origin, "ableToDoWhatNeeded" -> ableToDoWhatNeeded.getOrElse(""))

    auditConnector.sendExplicitAudit(auditType, auditMap)
  }

  def feedbackSurveyUsingServiceAudit(origin: String, beforeUsingThisService: List[String])(
    implicit hc: HeaderCarrier): Unit = {

    var option0, option1, option2, option3, option4, option5, option6: (String, String) = ("", "")
    if (beforeUsingThisService.lift(0).isDefined) { option0 = beforeUsingThisService.lift(0).get -> "Checked" }
    if (beforeUsingThisService.lift(1).isDefined) { option1 = beforeUsingThisService.lift(1).get -> "Checked" }
    if (beforeUsingThisService.lift(2).isDefined) { option2 = beforeUsingThisService.lift(2).get -> "Checked" }
    if (beforeUsingThisService.lift(3).isDefined) { option3 = beforeUsingThisService.lift(3).get -> "Checked" }
    if (beforeUsingThisService.lift(4).isDefined) { option4 = beforeUsingThisService.lift(4).get -> "Checked" }
    if (beforeUsingThisService.lift(5).isDefined) { option5 = beforeUsingThisService.lift(5).get -> "Checked" }
    if (beforeUsingThisService.lift(6).isDefined) { option6 = beforeUsingThisService.lift(6).get -> "Checked" }

    val auditMap = Map(
      "origin" -> origin,
      option0,
      option1,
      option2,
      option3,
      option4,
      option5,
      option6
    ).filter((t) => t._1 != "")

    auditConnector.sendExplicitAudit(auditType, auditMap)
  }

  def feedbackSurveyAboutServiceAudit(origin: String, serviceReceived: Option[String])(
    implicit hc: HeaderCarrier): Unit = {

    val auditMap = Map("origin" -> origin, "serviceReceived" -> serviceReceived.getOrElse(""))

    auditConnector.sendExplicitAudit(auditType, auditMap)
  }

  def feedbackSurveyRecommendServiceAudit(
    origin: String,
    reasonForRating: Option[String],
    recommendRating: Option[String])(implicit hc: HeaderCarrier): Unit = {

    val auditMap = Map(
      "origin"          -> origin,
      "reasonForRating" -> reasonForRating.getOrElse(""),
      "recommendRating" -> recommendRating.getOrElse(""))

    auditConnector.sendExplicitAudit(auditType, auditMap)
  }
}
