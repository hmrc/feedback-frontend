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

package forms

import forms.behaviours.{BooleanFieldBehaviours, CheckboxFieldBehaviours, OptionFieldBehaviours}
import models._
import models.eotho._
import play.api.data.FormError

class EOTHOQuestionsFormProviderSpec
    extends OptionFieldBehaviours with CheckboxFieldBehaviours with BooleanFieldBehaviours {

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

  ".numberOfEmployees" must {

    val fieldName = "numberOfEmployees"
    val invalidError = "error.invalid"

    behave like optionsField[EOTHOQuestions, NumberOfEmployeesQuestion](
      form,
      fieldName,
      NumberOfEmployeesQuestion.values,
      FormError(fieldName, invalidError),
      _.numberOfEmployees
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

  ".protectAtRiskJobs" must {

    val fieldName = "protectAtRiskJobs"
    val invalidError = "error.boolean"

    behave like booleanField[EOTHOQuestions](
      form,
      fieldName,
      invalidError = FormError(fieldName, invalidError),
      _.protectAtRiskJobs
    )
  }

  ".protectHospitalityIndustry" must {

    val fieldName = "protectHospitalityIndustry"
    val invalidError = "error.boolean"

    behave like booleanField[EOTHOQuestions](
      form,
      fieldName,
      invalidError = FormError(fieldName, invalidError),
      _.protectHospitalityIndustry
    )
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

  ".encourageReopenSooner" must {

    val fieldName = "encourageReopenSooner"
    val invalidError = "error.boolean"

    behave like booleanField[EOTHOQuestions](
      form,
      fieldName,
      invalidError = FormError(fieldName, invalidError),
      _.encourageReopenSooner
    )
  }

  ".encourageReturnToRestaurantsSooner" must {

    val fieldName = "encourageReturnToRestaurantsSooner"
    val invalidError = "error.boolean"

    behave like booleanField[EOTHOQuestions](
      form,
      fieldName,
      invalidError = FormError(fieldName, invalidError),
      _.encourageReturnToRestaurantsSooner
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

  ".offerDiscounts" must {

    val fieldName = "offerDiscounts"
    val invalidError = "error.invalid"

    behave like optionsField[EOTHOQuestions, OfferDiscountsQuestion](
      form,
      fieldName,
      OfferDiscountsQuestion.values,
      FormError(fieldName, invalidError),
      _.offerDiscounts
    )
  }
}
