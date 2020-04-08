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

import javax.inject.Inject
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import config.FrontendAppConfig
import forms.PTAQuestionsFormProvider
import models.{FeedbackId, Origin, PTAQuestions}
import navigation.Navigator
import pages.GenericQuestionsPage
import play.api.mvc.Action
import services.AuditService
import views.html.ptaQuestions

import scala.concurrent.ExecutionContext

class PTAQuestionsController @Inject()(
  appConfig: FrontendAppConfig,
  override val messagesApi: MessagesApi,
  navigator: Navigator,
  formProvider: PTAQuestionsFormProvider,
  auditService: AuditService)(implicit ec: ExecutionContext)
    extends FrontendController with I18nSupport {

  val form: Form[PTAQuestions] = formProvider()
  def submitCall(origin: Origin) = routes.PTAQuestionsController.onSubmit(origin)

  def onPageLoad(origin: Origin) = Action { implicit request =>
    Ok(ptaQuestions(appConfig, form, submitCall(origin)))
  }

  def onSubmit(origin: Origin) = Action { implicit request =>
    form
      .bindFromRequest()
      .fold(
        formWithErrors => BadRequest(ptaQuestions(appConfig, formWithErrors, submitCall(origin))),
        value => {
          auditService.ptaAudit(origin, FeedbackId.fromSession, value)
          Redirect(navigator.nextPage(GenericQuestionsPage)(origin))
        }
      )
  }
}
