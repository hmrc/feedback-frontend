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

///*
// * Copyright 2020 HM Revenue & Customs
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
package forms

import forms.behaviours.{BooleanFieldBehaviours, OptionFieldBehaviours, StringFieldBehaviours}
import models.{EOTHOQuestions, NumberOfEstablishmentsQuestion}
import play.api.data.FormError

class EOTHOQuestionsFormProviderSpec extends OptionFieldBehaviours {

  def form = new EOTHOQuestionsFormProvider()()

  ".numberOfEstablishments" must {

    val fieldName = "numberOfEstablishments"
    val invalidError = "error.invalid"

    behave like optionsField[EOTHOQuestions, NumberOfEstablishmentsQuestion](
      form,
      fieldName,
      NumberOfEstablishmentsQuestion.values,
      FormError(fieldName, invalidError),
      _.numberOfEstablishments
    )
  }
}
