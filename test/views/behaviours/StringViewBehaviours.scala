/*
 * Copyright 2018 HM Revenue & Customs
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

package views.behaviours

import play.api.data.{Form, FormError}
import play.twirl.api.HtmlFormat

trait StringViewBehaviours[A] extends QuestionViewBehaviours[A] {

  val answer = "answer"

  def stringPage(createView: Form[A] => HtmlFormat.Appendable,
                 fieldName: String,
                 messageKeyPrefix: String,
                 expectedHintKey: Option[String] = None) = {

    s"behave like a page with a string value field of '$fieldName'" when {
      "rendered" must {

        "contain a label for the value" in {
          val doc = asDocument(createView(form))
          val expectedHintText = expectedHintKey map(k => messages(k))
          assertContainsLabel(doc, fieldName, messages(s"$messageKeyPrefix.heading"), expectedHintText)
        }

        "contain an input for the value" in {
          val doc = asDocument(createView(form))
          assertRenderedById(doc, fieldName)
        }
      }

      "rendered with a valid form" must {
        "include the form's value in the value input" in {
          val boundForm = form.bind(Map(fieldName -> answer))
          val doc = asDocument(createView(boundForm))

          doc.getElementById(fieldName).`val`() mustBe answer
        }
      }

      "rendered with an error" must {

        "show an error summary" in {
          val doc = asDocument(createView(form.withError(error)))
          assertRenderedById(doc, "error-summary-heading")
        }

        "show an error in the value field's label" in {
          val doc = asDocument(createView(form.withError(FormError(fieldName, errorMessage))))
          val errorSpan = doc.getElementsByClass("error-message").first
          errorSpan.text mustBe messages(errorMessage)
        }

        "show an error prefix in the browser title" in {
          val doc = asDocument(createView(form.withError(error)))
          assertContainsValue(doc, "title", messages("error.browser.title.prefix"))
        }
      }
    }
  }
}
