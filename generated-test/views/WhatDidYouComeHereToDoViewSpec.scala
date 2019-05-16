package views

import play.api.data.Form
import controllers.routes
import forms.GiveCommentsFormProvider
import models.NormalMode
import views.behaviours.StringViewBehaviours
import views.html.whatDidYouComeHereToDo

class WhatDidYouComeHereToDoViewSpec extends StringViewBehaviours {

  val messageKeyPrefix = "whatDidYouComeHereToDo"

  val form = new GiveCommentsFormProvider()()

  def createView = () => whatDidYouComeHereToDo(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[String]) => whatDidYouComeHereToDo(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  "WhatDidYouComeHereToDo view" must {
    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)

    behave like stringPage(createViewUsingForm, messageKeyPrefix, routes.WhatDidYouComeHereToDoController.onSubmit(NormalMode).url)
  }
}
