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

package controllers

import javax.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import config.FrontendAppConfig
import models.Origin
import play.api.mvc.Action
import views.html.{thankYou, thankYouPension}

class ThankYouController @Inject()(appConfig: FrontendAppConfig,
                                   override val messagesApi: MessagesApi
                                   ) extends FrontendController with I18nSupport {

  def onPageLoadWithOrigin(origin: Origin) = onPageLoad()

  def onPageLoad = Action {
    implicit request =>
      Ok(thankYou(appConfig))
  }

  def onPageLoadPension = Action {
    implicit request =>
      Ok(thankYouPension(appConfig))
  }
}
