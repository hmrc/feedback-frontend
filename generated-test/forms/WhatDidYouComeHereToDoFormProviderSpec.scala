package forms

import forms.behaviours.StringFieldBehaviours
import play.api.data.FormError

class WhatDidYouComeHereToDoFormProviderSpec extends StringFieldBehaviours {

  val requiredKey = "whatDidYouComeHereToDo.error.required"
  val lengthKey = "whatDidYouComeHereToDo.error.length"
  val maxLength = 1000

  val form = new GiveCommentsFormProvider()()

  ".value" must {

    val fieldName = "value"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      stringsWithMaxLength(maxLength)
    )

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength = maxLength,
      lengthError = FormError(fieldName, lengthKey, Seq(maxLength))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
