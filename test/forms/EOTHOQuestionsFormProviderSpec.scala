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

import forms.behaviours.{CheckboxFieldBehaviours, OptionFieldBehaviours}
import models._
import play.api.data.FormError

class EOTHOQuestionsFormProviderSpec extends OptionFieldBehaviours with CheckboxFieldBehaviours {

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

  for {
    (value, i) <- WhichRegionQuestion.values.zipWithIndex
  } yield
    "whichRegions" must {
      s"binds `$value` successfully" in {
        val fieldName = "whichRegions"
        val data = Map(
          s"$fieldName[$i]" -> value.toString
        )

        form.bind(data).get.whichRegions shouldEqual List(value)
      }
    }

  ".comparedToMonTueWed" must {

    val fieldName = "comparedToMonTueWed"
    val invalidError = "error.invalid"

    behave like optionsField[EOTHOQuestions, ComparedToMonTueWedQuestion](
      form,
      fieldName,
      ComparedToMonTueWedQuestion.values,
      FormError(fieldName, invalidError),
      _.comparedToMonTueWed
    )
  }

  ".comparedToThurFriSatSun" must {

    val fieldName = "comparedToThurFriSatSun"
    val invalidError = "error.invalid"

    behave like optionsField[EOTHOQuestions, ComparedToThurFriSatSunQuestion](
      form,
      fieldName,
      ComparedToThurFriSatSunQuestion.values,
      FormError(fieldName, invalidError),
      _.comparedToThurFriSatSun
    )
  }

  ".comparedBusinessTurnover" must {

    val fieldName = "comparedBusinessTurnover"
    val invalidError = "error.invalid"

    behave like optionsField[EOTHOQuestions, ComparedBusinessTurnoverQuestion](
      form,
      fieldName,
      ComparedBusinessTurnoverQuestion.values,
      FormError(fieldName, invalidError),
      _.comparedBusinessTurnover
    )
  }

  ".affectedJobs" must {

    val fieldName = "affectedJobs"
    val invalidError = "error.invalid"

    behave like optionsField[EOTHOQuestions, AffectedJobsQuestion](
      form,
      fieldName,
      AffectedJobsQuestion.values,
      FormError(fieldName, invalidError),
      _.affectedJobs
    )
  }

  ".furloughEmployees" must {

    val fieldName = "furloughEmployees"
    val invalidError = "error.invalid"

    behave like optionsField[EOTHOQuestions, FurloughEmployeesQuestion](
      form,
      fieldName,
      FurloughEmployeesQuestion.values,
      FormError(fieldName, invalidError),
      _.furloughEmployees
    )
  }

  ".businessFuturePlans" must {

    val fieldName = "businessFuturePlans"
    val invalidError = "error.invalid"

    behave like optionsField[EOTHOQuestions, BusinessFuturePlansQuestion](
      form,
      fieldName,
      BusinessFuturePlansQuestion.values,
      FormError(fieldName, invalidError),
      _.businessFuturePlans
    )
  }
}
