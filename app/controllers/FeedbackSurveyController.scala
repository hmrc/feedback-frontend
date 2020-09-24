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

package controllers

import com.google.inject.Inject
import models.{Origin, formMappings}
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, MessagesControllerComponents}
import services.{AuditService, OriginService}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

class FeedbackSurveyController @Inject()(
  auditService: AuditService,
  originService: OriginService,
  mcc: MessagesControllerComponents)
    extends FrontendController(mcc) with I18nSupport {

  def feedbackRedirect(origin: String) = Action { implicit request =>
    ptaRedirect(origin)
  }

  def feedbackRedirect(origin: Origin) = Action { implicit request =>
    ptaRedirect(origin.value)
  }

  def ableToDoContinue(origin: String) = Action(parse.form(formMappings.ableToDoForm)) { implicit request =>
    auditService.feedbackSurveyAbleToDoAudit(origin, request.body.ableToDoWhatNeeded)
    ptaRedirect(origin)
  }

  def usingServiceContinue(origin: String) = Action(parse.form(formMappings.usingServiceForm)) { implicit request =>
    auditService.feedbackSurveyUsingServiceAudit(origin, request.body.beforeUsingThisService)
    ptaRedirect(origin)
  }

  def aboutServiceContinue(origin: String) = Action(parse.form(formMappings.aboutServiceForm)) { implicit request =>
    auditService.feedbackSurveyAboutServiceAudit(origin, request.body.serviceReceived)
    ptaRedirect(origin)
  }

  def recommendServiceContinue(origin: String) = Action(parse.form(formMappings.recommendServiceForm)) {
    implicit request =>
      auditService
        .feedbackSurveyRecommendServiceAudit(origin, request.body.reasonForRating, request.body.recommendRating)
      originService.customFeedbackUrl(origin) match {
        case Some(x) => Redirect(x)
        case None    => ptaRedirect(origin)
      }
  }

  def ptaRedirect(origin: String) = {

    val ptaRedirects = Seq(
      "CARBEN",
      "FANDF",
      "MEDBEN",
      "NISP",
      "P800",
      "PERTAX",
      "REPAYMENTS",
      "PLA",
      "TAI",
      "TCR",
      "TCS",
      "TCSHOME",
      "TES",
      "TYF")

    if (ptaRedirects.contains(origin))
      Redirect(routes.PTAQuestionsController.onPageLoad(Origin.fromString(origin)))
    else
      Redirect(routes.OtherQuestionsController.onPageLoad(Origin.fromString(origin)))

  }
}
