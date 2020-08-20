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

package views

import forms.BTAQuestionsFormProvider
import models.BTAQuestions
import play.api.data.Form
import views.behaviours.StringViewBehaviours
import views.html.giveComments

class GiveCommentsViewSpec extends StringViewBehaviours[BTAQuestions] {

  val messageKeyPrefix = "giveComments"

  val form = new BTAQuestionsFormProvider()()
  val action = controllers.routes.SessionExpiredController.onPageLoad()

  def createView = () => createViewUsingForm(form)

  def createViewUsingForm =
    (form: Form[_]) => giveComments(frontendAppConfig, form, action)(fakeRequest, messages)

  "GiveComments view" must {
    behave like normalPage(createView, messageKeyPrefix, "intro1")

    behave like stringPage(createViewUsingForm, "value", messageKeyPrefix)

  }
}
