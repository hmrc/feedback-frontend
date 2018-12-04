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

package controllers

import javax.inject.Inject
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import config.FrontendAppConfig
import forms.BTAQuestionsFormProvider
import models.BTAQuestions
import navigation.Navigator
import pages.GenericQuestionsPage
import play.api.mvc.Action
import views.html.btaQuestions
import services.AuditService

import scala.concurrent.ExecutionContext

class BTAQuestionsController @Inject()(appConfig: FrontendAppConfig,
                                       override val messagesApi: MessagesApi,
                                       navigator: Navigator,
                                       formProvider: BTAQuestionsFormProvider,
                                       auditService: AuditService
                                        )(implicit ec: ExecutionContext) extends FrontendController with I18nSupport {

  val form: Form[BTAQuestions] = formProvider()
  lazy val successPage = navigator.nextPage(GenericQuestionsPage)(())
  def submitCall(origin: String) = routes.BTAQuestionsController.onSubmit(origin)

  def onPageLoad(origin: String) = Action {
    implicit request =>
      Ok(btaQuestions(appConfig, form, submitCall(origin)))
  }

  def onSubmit(origin: String) = Action {
    implicit request =>

      form.bindFromRequest().fold(
        formWithErrors =>
          BadRequest(btaQuestions(appConfig, formWithErrors, submitCall(origin))),
        value => {
          auditService.btaAudit(origin, request.session.get("feedbackId").getOrElse("-"), value)
          Redirect(successPage)
        }
      )
  }
}