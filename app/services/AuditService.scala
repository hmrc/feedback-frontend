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

import javax.inject.Inject
import models._
import models.ccg._
import play.api.libs.json.Json
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

  def withIsAgent(ableToDo: Option[Boolean]): MapCont =
    _ + ("isAgent" -> ableToDo.map(boolToInt(_).toString).getOrElse("-"))

  def withTryingToDo(tryingToDo: Option[TryingToDoQuestion]): MapCont =
    _ + ("tryingToDo" -> tryingToDo.map(_.toString).getOrElse("-"))

  def withTryingToDoOther(tryingToDoOther: Option[String]): MapCont =
    _ + ("tryingToDoOther" -> tryingToDoOther.getOrElse("-"))

  def withWhyNotAbleToDo(whyNotAbleToDO: Option[String]): MapCont =
    _ + ("whyNotAbleToDo" -> whyNotAbleToDO.getOrElse("-"))

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

  def withCheckUnderstanding(checkUnderstanding: Option[CheckUnderstandingQuestion]): MapCont =
    _ + ("checkUnderstanding" -> checkUnderstanding.map(_.toString).getOrElse("-"))

  def withTreatedProfessionally(treatedProfessionally: Option[TreatedProfessionallyQuestion]): MapCont =
    _ + ("treatedProfessionally" -> treatedProfessionally.map(_.toString).getOrElse("-"))

  def withWhyGiveAnswer(whyGiveAnswer: Option[String]): MapCont =
    _ + ("whyGiveAnswer" -> whyGiveAnswer.getOrElse("-"))

  def withSupportFuture(supportFuture: Option[SupportFutureQuestion]): MapCont =
    _ + ("supportFuture" -> supportFuture.map(_.toString).getOrElse("-"))

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

  def trustsAudit(origin: Origin, feedbackId: FeedbackId, questions: TrustsQuestions)(
    implicit hc: HeaderCarrier): Unit = {

    val auditMap = (
      withOrigin(origin) andThen
        withFeedbackId(feedbackId) andThen
        withIsAgent(questions.isAgent) andThen
        withTryingToDo(questions.tryingToDo) andThen
        withTryingToDoOther(questions.tryingToDoOther) andThen
        withAbleToDo(questions.ableToDo) andThen
        withWhyNotAbleToDo(questions.whyNotAbleToDo) andThen
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

  def ccgAudit(origin: Origin, feedbackId: FeedbackId, questions: CCGQuestions)(implicit hc: HeaderCarrier): Unit = {

    val auditMap = (
      withOrigin(origin) andThen
        withFeedbackId(feedbackId) andThen
        withCheckUnderstanding(questions.complianceCheckUnderstanding) andThen
        withTreatedProfessionally(questions.treatedProfessionally) andThen
        withWhyGiveAnswer(questions.whyGiveAnswer) andThen
        withSupportFuture(questions.supportFutureTaxQuestion)
    )(emptyMap)

    auditConnector.sendExplicitAudit(auditType, auditMap)
  }

  def nmwCcgAudit(origin: Origin, feedbackId: FeedbackId, questions: NmwCcgQuestions)(
    implicit hc: HeaderCarrier): Unit = {

    val auditMap = (
      withOrigin(origin) andThen
        withFeedbackId(feedbackId) andThen
        withTreatedProfessionally(questions.treatedProfessionally) andThen
        withCheckUnderstanding(questions.checkUnderstanding) andThen
        withWhyGiveAnswer(questions.whyGiveAnswer) andThen
        withSupportFuture(questions.supportFutureNmw)
    )(emptyMap)

    auditConnector.sendExplicitAudit(auditType, auditMap)
  }
}
