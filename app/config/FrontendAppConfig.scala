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

package config

import com.google.inject.{Inject, Singleton}
import play.api.{Configuration, Environment}
import play.api.i18n.Lang
import controllers.routes
import uk.gov.hmrc.play.config.ServicesConfig

@Singleton
class FrontendAppConfig @Inject() (override val runModeConfiguration: Configuration, environment: Environment) extends ServicesConfig {

  override protected def mode = environment.mode

  private def loadConfig(key: String) = runModeConfiguration.getString(key).getOrElse(throw new Exception(s"Missing configuration key: $key"))

  private lazy val contactHost = runModeConfiguration.getString("contact-frontend.host").getOrElse("")
  private val contactFormServiceIdentifier = "feedbackfrontend"

  lazy val analyticsToken = loadConfig(s"google-analytics.token")
  lazy val analyticsHost = loadConfig(s"google-analytics.host")
  lazy val gtmContainerId = loadConfig(s"google-tag-manager.containerId")
  lazy val reportAProblemPartialUrl = s"$contactHost/contact/problem_reports_ajax?service=$contactFormServiceIdentifier"
  lazy val reportAProblemNonJSUrl = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"
  lazy val betaFeedbackUrl = s"$contactHost/contact/beta-feedback"
  lazy val betaFeedbackUnauthenticatedUrl = s"$contactHost/contact/beta-feedback-unauthenticated"

  lazy val authUrl = baseUrl("auth")
  lazy val loginUrl = loadConfig("urls.login")
  lazy val loginContinueUrl = loadConfig("urls.loginContinue")

  lazy val privacyPolicyUrl = "https://www.gov.uk/government/publications/data-protection-act-dpa-information-hm-revenue-and-customs-hold-about-you/data-protection-act-dpa-information-hm-revenue-and-customs-hold-about-you"
  lazy val urLinkUrl = runModeConfiguration.getString("microservice.services.features.ur-link-url")

  lazy val pensionSignInUrl = runModeConfiguration.getString("urls.pension-sign-in")
  lazy val pensionRetirementUrl = runModeConfiguration.getString("urls.pension-retirement")
  lazy val pensionSideBarOneUrl = runModeConfiguration.getString("urls.pension-sidebar-link-one")
  lazy val pensionSideBarOneUrlGA = runModeConfiguration.getString("urls.pension-sidebar-link-one-ga")
  lazy val pensionSideBarTwoUrl = runModeConfiguration.getString("urls.pension-sidebar-link-two")
  lazy val pensionSideBarTwoUrlGA = runModeConfiguration.getString("urls.pension-sidebar-link-two-ga")
  lazy val pensionSideBarThreeUrl = runModeConfiguration.getString("urls.pension-sidebar-link-three")
  lazy val pensionSideBarThreeUrlGA = runModeConfiguration.getString("urls.pension-sidebar-link-three-ga")
  lazy val pensionSideBarFourUrl = runModeConfiguration.getString("urls.pension-sidebar-link-four")
  lazy val pensionSideBarFourUrlGA = runModeConfiguration.getString("urls.pension-sidebar-link-four-ga")
  lazy val pensionSideBarFiveUrl = runModeConfiguration.getString("urls.pension-sidebar-link-five")
  lazy val pensionSideBarFiveUrlGA = runModeConfiguration.getString("urls.pension-sidebar-link-five-ga")

  lazy val govUkUrl = loadConfig(s"urls.govUk")

  lazy val languageTranslationEnabled = runModeConfiguration.getBoolean("microservice.services.features.welsh-translation").getOrElse(true)
  def languageMap: Map[String, Lang] = Map("english" -> Lang("en"), "cymraeg" -> Lang("cy"))
  def routeToSwitchLanguage = (lang: String) => routes.LanguageSwitchController.switchToLanguage(lang)

  lazy val isGtmEnabled = runModeConfiguration.getBoolean("google-tag-manager.enabled").getOrElse(true)

}
