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

package config

import com.google.inject.{Inject, Singleton}
import controllers.routes
import play.api.i18n.Lang
import play.api.{ConfigLoader, Configuration}
import services.OriginConfigItem
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import scala.collection.JavaConversions._

@Singleton
class FrontendAppConfig @Inject()(val config: Configuration, servicesConfig: ServicesConfig) {

  private def getOptional[A](key: String)(implicit loader: ConfigLoader[A]): Option[A] =
    config.getOptional[A](key)

  private def loadConfig(key: String) =
    getOptional[String](key).getOrElse(throw new Exception(s"Missing configuration key: $key"))

  private lazy val contactHost = getOptional[String]("contact-frontend.host").getOrElse("")
  private val contactFormServiceIdentifier = "feedbackfrontend"

  lazy val originConfigItems: List[OriginConfigItem] =
    config.getConfigList("origin-services").map(_.toList).getOrElse(Nil).map { configItem =>
      OriginConfigItem(configItem.getString("token"), configItem.getString("customFeedbackUrl"))
    }

  lazy val analyticsToken = loadConfig(s"google-analytics.token")
  lazy val analyticsHost = loadConfig(s"google-analytics.host")
  lazy val gtmContainerId = loadConfig(s"google-tag-manager.containerId")
  lazy val reportAProblemPartialUrl = s"$contactHost/contact/problem_reports_ajax?service=$contactFormServiceIdentifier"
  lazy val reportAProblemNonJSUrl = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"
  lazy val betaFeedbackUrl = s"$contactHost/contact/beta-feedback"
  lazy val betaFeedbackUnauthenticatedUrl = s"$contactHost/contact/beta-feedback-unauthenticated"

  lazy val authUrl = servicesConfig.baseUrl("auth")
  lazy val loginUrl = loadConfig("urls.login")
  lazy val loginContinueUrl = loadConfig("urls.loginContinue")

  lazy val privacyPolicyUrl =
    "https://www.gov.uk/government/publications/data-protection-act-dpa-information-hm-revenue-and-customs-hold-about-you/data-protection-act-dpa-information-hm-revenue-and-customs-hold-about-you"
  lazy val urLinkUrl = getOptional[String]("microservice.services.features.ur-link-url")

  lazy val pensionSignInUrl = getOptional[String]("urls.pension.sign-in")
  lazy val pensionRetirementUrl = getOptional[String]("urls.pension.retirement")
  lazy val pensionSideBarOneUrl = getOptional[String]("urls.pension.sidebar.link-one")
  lazy val pensionSideBarOneUrlGA = getOptional[String]("urls.pension.sidebar.link-one-ga")
  lazy val pensionSideBarTwoUrl = getOptional[String]("urls.pension.sidebar.link-two")
  lazy val pensionSideBarTwoUrlGA = getOptional[String]("urls.pension.sidebar.link-two-ga")
  lazy val pensionSideBarThreeUrl = getOptional[String]("urls.pension.sidebar.link-three")
  lazy val pensionSideBarThreeUrlGA = getOptional[String]("urls.pension.sidebar.link-three-ga")
  lazy val pensionSideBarFourUrl = getOptional[String]("urls.pension.sidebar.link-four")
  lazy val pensionSideBarFourUrlGA = getOptional[String]("urls.pension.sidebar.link-four-ga")
  lazy val pensionSideBarFiveUrl = getOptional[String]("urls.pension.sidebar.link-five")
  lazy val pensionSideBarFiveUrlGA = getOptional[String]("urls.pension.sidebar.link-five-ga")

  lazy val govUkUrl = loadConfig(s"urls.govUk")

  lazy val languageTranslationEnabled =
    getOptional[Boolean]("microservice.services.features.welsh-translation").getOrElse(true)
  def languageMap: Map[String, Lang] = Map("english" -> Lang("en"), "cymraeg" -> Lang("cy"))
  def routeToSwitchLanguage = (lang: String) => routes.LanguageSwitchController.switchToLanguage(lang)

  lazy val isGtmEnabled = getOptional[Boolean]("google-tag-manager.enabled").getOrElse(true)

}
