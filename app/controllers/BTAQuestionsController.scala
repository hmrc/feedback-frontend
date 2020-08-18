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

import config.FrontendAppConfig
import forms.BTAQuestionsFormProvider
import javax.inject.Inject
import models.{BTAQuestions, FeedbackId, Origin}
import navigation.Navigator
import pages.GenericQuestionsPage
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.MessagesControllerComponents
import services.AuditService
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.btaQuestions

class BTAQuestionsController @Inject()(
  appConfig: FrontendAppConfig,
  navigator: Navigator,
  formProvider: BTAQuestionsFormProvider,
  auditService: AuditService,
  mcc: MessagesControllerComponents)
    extends FrontendController(mcc) with I18nSupport {

  val form: Form[BTAQuestions] = formProvider()
  def submitCall(origin: Origin) = routes.BTAQuestionsController.onSubmit(origin)

  def onPageLoad(origin: Origin) = Action { implicit request =>
    Ok(btaQuestions(appConfig, form, submitCall(origin)))
  }

  def onSubmit(origin: Origin) = Action { implicit request =>
    form
      .bindFromRequest()
      .fold(
        formWithErrors => BadRequest(btaQuestions(appConfig, formWithErrors, submitCall(origin))),
        value => {
          auditService.btaAudit(origin, FeedbackId.fromSession, value)
          Redirect(navigator.nextPage(GenericQuestionsPage)(origin))
        }
      )
  }
}
