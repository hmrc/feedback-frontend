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

package models

import base.BaseSpec
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest

class CidSpec extends BaseSpec {

  "Cid" when {
    "fromUrl is called" must {
      "return the contents of the referral header following '?cid=' if the url argument exists" in {
        val cid = "123456789"

        implicit val request: FakeRequest[AnyContentAsEmpty.type] =
          FakeRequest().withHeaders("referer" -> s"/feedback/EXAMPLE?cid=$cid")

        Cid.fromUrl.value mustEqual cid
      }
      "return '-' if the referral header doesn't contain '?cid='" in {
        implicit val request: FakeRequest[AnyContentAsEmpty.type] =
          FakeRequest().withHeaders("referer" -> s"/feedback/EXAMPLE")

        Cid.fromUrl.value mustEqual "-"
      }
      "return '-' if the referral header contains '?cid=' but no value" in {
        implicit val request: FakeRequest[AnyContentAsEmpty.type] =
          FakeRequest().withHeaders("referer" -> s"/feedback/EXAMPLE?cid=")

        Cid.fromUrl.value mustEqual "-"
      }
      "return '-' if there is no referral header" in {
        implicit val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()

        Cid.fromUrl.value mustEqual "-"
      }
    }
  }
}
