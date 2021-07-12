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

package controllers

import config.FrontendAppConfig
import javax.inject.Inject
import models.Origin
import play.api.i18n.I18nSupport
import play.api.mvc.MessagesControllerComponents
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import views.html.{ThankYou, ThankYouPensionView}

class ThankYouController @Inject()(
  appConfig: FrontendAppConfig,
  mcc: MessagesControllerComponents,
  thankYou: ThankYou,
  thankYouPensionView: ThankYouPensionView)
    extends FrontendController(mcc) with I18nSupport {

  def onPageLoadWithOrigin(origin: Origin) = onPageLoad()

  def onPageLoad = Action { implicit request =>
    Ok(thankYou(appConfig))
  }

  def onPageLoadPension = Action { implicit request =>
    Ok(thankYouPensionView(appConfig))
  }
}
