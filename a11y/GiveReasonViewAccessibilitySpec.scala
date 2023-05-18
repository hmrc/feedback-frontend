import config.FrontendAppConfig
import forms.GiveReasonFormProvider
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.Form
import play.api.i18n.{Messages, MessagesApi}
import play.api.mvc.{AnyContentAsEmpty, Call}
import play.api.test.FakeRequest
import uk.gov.hmrc.scalatestaccessibilitylinter.AccessibilityMatchers
import views.html.GiveReasonView

class GiveReasonViewAccessibilitySpec
  extends AnyWordSpec
    with Matchers
    with GuiceOneAppPerSuite
    with AccessibilityMatchers {

  val appConfig: FrontendAppConfig = app.injector.instanceOf[FrontendAppConfig]
  val form: Form[_] = new GiveReasonFormProvider().apply()

  val method = "POST"
  val url = "https://www.tax.service.gov.uk/hmrc-frontend/:origin/give-Reason"
  val action: Call = Call(method, url)

  val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest("GET", "/hmrc-frontend/:origin/give-Reason")
  val messages: Messages = app.injector.instanceOf[MessagesApi].preferred(request)

  val pageTemplate: GiveReasonView = app.injector.instanceOf[GiveReasonView]

  "The Give Reason View page" must {

    "pass accessibility checks" in {
      val pageHtml = pageTemplate(
        appConfig,
        form,
        action
      )(
        request,
        messages
      ).toString()

      pageHtml must passAccessibilityChecks
    }

  }

}