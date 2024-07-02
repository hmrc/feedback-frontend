import play.core.PlayVersion
import sbt.*

object AppDependencies {

  val playVersion = "play-30"
  val bootStrapVersion = "8.5.0"

  val compile: Seq[ModuleID] = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc" %% s"play-frontend-hmrc-$playVersion"                 % "9.10.0",
    "uk.gov.hmrc" %% s"play-conditional-form-mapping-$playVersion"      % "2.0.0",
    "uk.gov.hmrc" %% s"bootstrap-frontend-$playVersion"                 % bootStrapVersion
  )

  val test: Seq[ModuleID]    = Seq(
    "uk.gov.hmrc"         %% s"bootstrap-test-$playVersion" % bootStrapVersion,
    "org.scalatest"       %% "scalatest"                    % "3.2.19",
    "org.playframework"   %% "play-test"                    % PlayVersion.current,
    "org.jsoup"            % "jsoup"                        % "1.17.2",
    "org.scalatestplus"   %% "scalatestplus-scalacheck"     % "3.1.0.0-RC2",
    "org.scalatestplus"   %% "scalatestplus-mockito"        % "1.0.0-M2",
    "com.vladsch.flexmark" % "flexmark-all"                 % "0.64.2" // Any version above this cause the accessibility tests to fail because it fetches com.ibm.icu::icu4j version 72.1
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test
}
