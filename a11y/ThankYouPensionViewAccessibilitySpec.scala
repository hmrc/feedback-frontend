import config.FrontendAppConfig
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.i18n.{Messages, MessagesApi}
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import uk.gov.hmrc.scalatestaccessibilitylinter.AccessibilityMatchers
import views.html.ThankYouPensionView

class ThankYouPensionViewAccessibilitySpec
  extends AnyWordSpec
    with Matchers
    with GuiceOneAppPerSuite
    with AccessibilityMatchers {

  val appConfig: FrontendAppConfig = app.injector.instanceOf[FrontendAppConfig]

  val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest("GET", "/thank-you-pension")
  val messages: Messages = app.injector.instanceOf[MessagesApi].preferred(request)

  val pageTemplate: ThankYouPensionView = app.injector.instanceOf[ThankYouPensionView]

  "The Thank You Pension View page" must {

    "pass accessibility checks" in {
      val pageHtml = pageTemplate(
        appConfig
      )(
        request,
        messages
      ).toString()

      pageHtml must passAccessibilityChecks
    }

  }

}