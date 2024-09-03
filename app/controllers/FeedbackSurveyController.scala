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

import com.google.inject.Inject
import models.Origin
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

class FeedbackSurveyController @Inject()(
                                          mcc: MessagesControllerComponents
                                        ) extends FrontendController(mcc) with I18nSupport {

  def feedbackRedirect(origin: String): Action[AnyContent] = Action { _ =>
    ptaRedirect(origin)
  }

  def feedbackHomePageRedirect: Action[AnyContent] = Action { _ =>
    Redirect(routes.OtherQuestionsController.onPageLoad(Origin.fromString("feedback")))
  }

  private def ptaRedirect(origin: String): Result = {

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
      "TYF"
    )

    if (ptaRedirects.contains(origin)) {
      Redirect(routes.PTAQuestionsController.onPageLoad(Origin.fromString(origin)))
    } else {
      Redirect(routes.OtherQuestionsController.onPageLoad(Origin.fromString(origin)))
    }

  }
}
