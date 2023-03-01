/*
 * Copyright 2023 HM Revenue & Customs
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
import uk.gov.hmrc.govukfrontend.views.viewmodels.radios.RadioItem
import views.ViewUtils

trait OptionsViewBehaviours[A] extends QuestionViewBehaviours[A] {

  def optionsPage(
    createView: Form[A] => HtmlFormat.Appendable,
    fieldName: String,
    options: Seq[RadioItem],
    messageKeyPrefix: String,
    legendClass: String = "govuk-fieldset__legend--m") =
    s"behave like a page with a $fieldName radio options question" when {
      "rendered" must {
        "contain a legend for the question" in {
          val doc = asDocument(createView(form))
          val legends = doc.getElementsByClass(legendClass)
          legends.eachText() must contain(messages(s"$messageKeyPrefix.heading"))
        }

        "contain an input for the value" in {
          val doc = asDocument(createView(form))
          for (option <- options) {
            assertContainsRadioButton(doc, option.id.get, fieldName, option.value.get, false)
          }
        }

        "not render an error summary" in {
          val doc = asDocument(createView(form))
          assertNotRenderedByCssSelector(doc, ".govuk-error-summary__title")
        }
      }

      "rendered with an error" must {
        "show an error summary with error links" in {
          val doc = asDocument(createView(form.withError(FormError(fieldName, "error.invalid"))))
          assertRenderedByCssSelector(doc, ".govuk-error-summary__title")
          val errorLinksUl = doc.getElementsByClass("govuk-list govuk-error-summary__list").first()
          val errorLinkUrl = errorLinksUl.children().first().child(0).attr("href")
          errorLinkUrl mustBe s"""${ViewUtils.errorLinkId(fieldName, form)}"""
        }

        "show an error in the value field's label" in {
          val doc = asDocument(createView(form.withError(FormError(fieldName, "error.invalid"))))

          val errorSpan = doc.getElementsByClass("govuk-error-message").first
          errorSpan.text must include(messages("error.invalid"))
        }

        "show an error prefix in the browser title" in {
          val doc = asDocument(createView(form.withError(error)))
          assertContainsValue(doc, "title", messages("error.browser.title.prefix"))
        }
      }
    }
}
