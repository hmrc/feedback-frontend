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

package config

import com.google.inject.{Inject, Singleton}
import play.api.{ConfigLoader, Configuration}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

@Singleton
class FrontendAppConfig @Inject()(val runModeConfiguration: Configuration, servicesConfig: ServicesConfig) {

  private def getOptional[A](key: String)(implicit loader: ConfigLoader[A]): Option[A] =
    runModeConfiguration.getOptional[A](key)

  lazy val privacyPolicyUrl =
    "https://www.gov.uk/government/publications/data-protection-act-dpa-information-hm-revenue-and-customs-hold-about-you/data-protection-act-dpa-information-hm-revenue-and-customs-hold-about-you"
  lazy val pensionSignInUrl: Option[String] = getOptional[String]("urls.pension.sign-in")
  lazy val pensionRetirementUrl: Option[String] = getOptional[String]("urls.pension.retirement")
  lazy val pensionSideBarOneUrl: Option[String] = getOptional[String]("urls.pension.sidebar.link-one")
  lazy val pensionSideBarTwoUrl: Option[String] = getOptional[String]("urls.pension.sidebar.link-two")
  lazy val pensionSideBarThreeUrl: Option[String] = getOptional[String]("urls.pension.sidebar.link-three")
  lazy val pensionSideBarFourUrl: Option[String] = getOptional[String]("urls.pension.sidebar.link-four")
  lazy val pensionSideBarFiveUrl: Option[String] = getOptional[String]("urls.pension.sidebar.link-five")

}
