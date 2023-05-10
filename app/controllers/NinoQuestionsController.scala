/*
 * Copyright 2023 HM Revenue & Customs
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
import forms.NinoQuestionsFormProvider
import models.{FeedbackId, NinoQuestions, Origin}
import navigation.Navigator
import pages.GenericQuestionsPage
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.AuditService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import views.html.NinoQuestionsView

import javax.inject.Inject

class NinoQuestionsController @Inject()(
                                         appConfig: FrontendAppConfig,
                                         navigator: Navigator,
                                         formProvider: NinoQuestionsFormProvider,
                                         auditService: AuditService,
                                         mcc: MessagesControllerComponents,
                                         ninoQuestionsView: NinoQuestionsView)
  extends FrontendController(mcc) with I18nSupport {

  val form: Form[NinoQuestions] = formProvider()

  def submitCall(origin: Origin) = routes.NinoQuestionsController.onSubmit(origin)

  def onPageLoad(origin: Origin): Action[AnyContent] = Action { implicit request =>
    Ok(ninoQuestionsView(appConfig, form, submitCall(origin)))
  }

  def onSubmit(origin: Origin): Action[AnyContent] = Action { implicit request =>
    form
      .bindFromRequest()
      .fold(
        formWithErrors => BadRequest(ninoQuestionsView(appConfig, formWithErrors, submitCall(origin))),
        value => {
          auditService.ninoAudit(origin, FeedbackId.fromSession, value)
          Redirect(navigator.nextPage(GenericQuestionsPage)(origin))
        }
      )
  }
}