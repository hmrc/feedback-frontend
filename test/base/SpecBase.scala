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

package base

import config.FrontendAppConfig
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.*
import play.api.i18n.{Messages, MessagesApi}
import play.api.mvc.{AnyContentAsEmpty, MessagesControllerComponents}
import play.api.test.{FakeRequest, Injecting}
import uk.gov.hmrc.hmrcfrontend.config.ServiceNavigationConfig

import scala.concurrent.ExecutionContext

trait SpecBase extends PlaySpec with GuiceOneAppPerSuite with Injecting {
  val frontendAppConfig: FrontendAppConfig             = inject[FrontendAppConfig]
  val messagesApi: MessagesApi                         = inject[MessagesApi]
  val fakeRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest("", "")
  val mcc: MessagesControllerComponents                = inject[MessagesControllerComponents]

  given messages: Messages      = messagesApi.preferred(fakeRequest)
  given ec: ExecutionContext    = inject[ExecutionContext]
  given ServiceNavigationConfig = inject[ServiceNavigationConfig]
}
