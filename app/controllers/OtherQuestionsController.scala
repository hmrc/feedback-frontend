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
import forms.OtherQuestionsFormProvider
import models.{OtherQuestions, UserAnswers}
import navigation.Navigator
import pages.GenericQuestionsPage
import play.api.mvc.Action
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import views.html.otherQuestions

class OtherQuestionsController @Inject()(appConfig: FrontendAppConfig,
                                         override val messagesApi: MessagesApi,
                                         navigator: Navigator,
                                         formProvider: OtherQuestionsFormProvider,
                                         auditConnector: AuditConnector
                                         ) extends FrontendController with I18nSupport {

  val form: Form[OtherQuestions] = formProvider()
  lazy val successPage = navigator.nextPage(GenericQuestionsPage)(())
  def submitCall(origin: String) = routes.OtherQuestionsController.onSubmit(origin)

  def onPageLoad(origin: String) = Action {
    implicit request =>
      Ok(otherQuestions(appConfig, form, submitCall(origin)))
  }

  def onSubmit(origin: String) = Action {
    implicit request =>

      form.bindFromRequest().fold(
        formWithErrors =>
          BadRequest(otherQuestions(appConfig, formWithErrors, submitCall(origin))),
        value => {

          val auditMap =
            Map(
              "origin"            -> origin,
              "ableToDo"          -> value.ableToDo.map(_.toString).getOrElse("-"),
              "howEasyScore"      -> value.howEasyScore.map(_.value.toString).getOrElse("-"),
              "whyGiveScore"      -> value.whyGiveScore.getOrElse("-"),
              "howDoYouFeelScore" -> value.howDoYouFeelScore.map(_.value.toString).getOrElse("-")
            )

          auditConnector.sendExplicitAudit("feedback", auditMap)

          Redirect(successPage)
        }
      )
  }
}
