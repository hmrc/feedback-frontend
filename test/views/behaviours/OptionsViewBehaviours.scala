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
import viewmodels.RadioOption

trait OptionsViewBehaviours[A] extends QuestionViewBehaviours[A] {

  def optionsPage(createView: Form[A] => HtmlFormat.Appendable,
                  fieldName: String,
                  options: Seq[RadioOption],
                  messageKeyPrefix: String) = {

    s"behave like a page with a $fieldName radio options question" when {
      "rendered" must {
        "contain a legend for the question" in {
          val doc = asDocument(createView(form))
          val legends = doc.select(s"#$fieldName legend")
          legends.size mustBe 1
          legends.first.text mustBe messages(s"$messageKeyPrefix.heading")
        }

        "contain an input for the value" in {
          val doc = asDocument(createView(form))
          for (option <- options) {
            assertContainsRadioButton(doc, option.id, fieldName, option.value, false)
          }
        }

        "not render an error summary" in {
          val doc = asDocument(createView(form))
          assertNotRenderedById(doc, "error-summary_header")
        }
      }


      for(option <- options) {
        s"rendered with a $fieldName of '${option.value}'" must {
          s"have the '${option.value}' radio button selected" in {
            val doc = asDocument(createView(form.bind(Map(fieldName -> s"${option.value}"))))
            assertContainsRadioButton(doc, option.id, fieldName, option.value, true)

            for(unselectedOption <- options.filterNot(o => o == option)) {
              assertContainsRadioButton(doc, unselectedOption.id, fieldName, unselectedOption.value, false)
            }
          }
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
