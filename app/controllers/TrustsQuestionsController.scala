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

package controllers

import config.FrontendAppConfig
import forms.TrustsQuestionsFormProvider
import models.{FeedbackId, Origin, TrustsQuestions}
import navigation.Navigator
import pages.GenericQuestionsPage
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, Call, MessagesControllerComponents}
import services.AuditService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import views.html.TrustsQuestionsView

import javax.inject.Inject

class TrustsQuestionsController @Inject() (
  appConfig: FrontendAppConfig,
  navigator: Navigator,
  formProvider: TrustsQuestionsFormProvider,
  auditService: AuditService,
  mcc: MessagesControllerComponents,
  trustsQuestions: TrustsQuestionsView
) extends FrontendController(mcc)
    with I18nSupport {

  val form: Form[TrustsQuestions] = formProvider()

  def submitCall(): Call = routes.TrustsQuestionsController.onSubmit()

  def onPageLoad(): Action[AnyContent] = Action { implicit request =>
    Ok(trustsQuestions(appConfig, form, submitCall()))
  }

  def onSubmit(): Action[AnyContent] = Action { implicit request =>
    val origin: Origin = Origin.fromString("trusts")

    form
      .bindFromRequest()
      .fold(
        formWithErrors => BadRequest(trustsQuestions(appConfig, formWithErrors, submitCall())),
        value => {
          auditService.trustsAudit(origin, FeedbackId.fromSession, value)
          Redirect(navigator.nextPage(GenericQuestionsPage)(origin))
        }
      )
  }
}
