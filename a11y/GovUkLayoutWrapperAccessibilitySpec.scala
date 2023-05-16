import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.i18n.{Messages, MessagesApi}
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import play.twirl.api.Html
import uk.gov.hmrc.scalatestaccessibilitylinter.AccessibilityMatchers
import views.html.GovUKLayoutWrapper

class GovUkLayoutWrapperAccessibilitySpec
  extends AnyWordSpec
    with Matchers
    with GuiceOneAppPerSuite
    with AccessibilityMatchers {

  val pageTitle = "Main Page"
  val showBackLink = true

  val content: Html = Html("")

  val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest("GET", "/")
  val messages: Messages = app.injector.instanceOf[MessagesApi].preferred(request)

  val pageTemplate: GovUKLayoutWrapper = app.injector.instanceOf[GovUKLayoutWrapper]

  "The Gov UK Layout Wrapper page" must {

    "pass accessibility checks" in {
      val pageHtml = pageTemplate(
        pageTitle,
        None,
        None,
        showBackLink
      )(
        content
      )(
        request,
        messages
      ).toString()

      pageHtml must passAccessibilityChecks
    }

  }

}