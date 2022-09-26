/*
 * Copyright 2022 HM Revenue & Customs
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

package views

import handlers.ErrorHandler
import models.ccg.{CheckUnderstandingQuestion, SupportFutureQuestion, TreatedProfessionallyQuestion}
import models.{CCGQuestions, Origin}
import play.api.data.Form
import views.behaviours._
import views.html.GlobalNotFoundView

class GlobalNotFoundViewSpec extends ViewBehaviours {

  val messageKeyPrefix = "page_not_found"

  val errorHandler = app.injector.instanceOf[ErrorHandler]
  val action = errorHandler.notFoundTemplate(fakeRequest)

  lazy val globalNotFoundView = inject[GlobalNotFoundView]

  def createView = () => globalNotFoundView(frontendAppConfig)(fakeRequest, messages)

  "GlobalNotFound view" must {
    behave like normalPage(createView, messageKeyPrefix, "govuk-heading-xl", "heading")

    "contain relevant information paragraph" in {
      val expectedMessage = messages(
        "page.not.found.error.check.web.address.correct",
        messages("page.not.found.error.check.web.address.full"))
      val doc = asDocument(createView())
      assertContainsText(doc, expectedMessage)
    }
  }
}
