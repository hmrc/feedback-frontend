import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.i18n.{Messages, MessagesApi}
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import uk.gov.hmrc.scalatestaccessibilitylinter.AccessibilityMatchers
import views.html.SessionExpiredView

class SessionExpiredViewAccessibilitySpec
  extends AnyWordSpec
    with Matchers
    with GuiceOneAppPerSuite
    with AccessibilityMatchers {

  val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest("GET", "/this-service-has-been-reset")
  val messages: Messages = app.injector.instanceOf[MessagesApi].preferred(request)

  val pageTemplate: SessionExpiredView = app.injector.instanceOf[SessionExpiredView]

  "The Session Expired View page" must {

    "pass accessibility checks" in {
      val pageHtml = pageTemplate(

      )(
        request,
        messages
      ).toString()

      pageHtml must passAccessibilityChecks
    }

  }

}