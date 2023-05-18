import config.FrontendAppConfig
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.i18n.{Messages, MessagesApi}
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import uk.gov.hmrc.scalatestaccessibilitylinter.AccessibilityMatchers
import views.html.ErrorTemplateView

class ErrorTemplateViewAccessibilitySpec
  extends AnyWordSpec
    with Matchers
    with GuiceOneAppPerSuite
    with AccessibilityMatchers {

  val pageTitle: String = "Some Random Page"
  val heading: String = "Welcome to Some Random Page !"
  val message: String = "This is an error message !"

  val appConfig: FrontendAppConfig = app.injector.instanceOf[FrontendAppConfig]

  val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest("GET", "/")
  val messages: Messages = app.injector.instanceOf[MessagesApi].preferred(request)

  val pageTemplate: ErrorTemplateView = app.injector.instanceOf[ErrorTemplateView]

  "The Error Template View page" must {

    "pass accessibility checks" in {
      val pageHtml = pageTemplate(
        pageTitle,
        heading,
        message,
        appConfig
      )(
        request,
        messages
      ).toString()

      pageHtml must passAccessibilityChecks
    }

  }

}