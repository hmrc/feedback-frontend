import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.i18n.{Messages, MessagesApi}
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import play.twirl.api.Html
import uk.gov.hmrc.scalatestaccessibilitylinter.AccessibilityMatchers
import views.html.MainTemplate

class MainTemplateAccessibilitySpec
  extends AnyWordSpec
    with Matchers
    with GuiceOneAppPerSuite
    with AccessibilityMatchers {

  val title = "Main Page"
  val showBackLink = true

  val contentBlock: Html = Html("")

  val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest("GET", "/")
  val messages: Messages = app.injector.instanceOf[MessagesApi].preferred(request)

  val pageTemplate: MainTemplate = app.injector.instanceOf[MainTemplate]

  "The Main Template page" must {

    "pass accessibility checks" in {
      val pageHtml = pageTemplate(
        title,
        None,
        None,
        None,
        None,
        None,
        showBackLink
      )(
        contentBlock
      )(
        request,
        messages
      ).toString()

      pageHtml must passAccessibilityChecks
    }

  }

}