import sbt.*

object AppDependencies {

  val playVersion = "play-30"
  val bootStrapVersion = "9.3.0"

  val compile: Seq[ModuleID] = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc" %% s"play-frontend-hmrc-$playVersion"                 % "10.6.0",
    "uk.gov.hmrc" %% s"play-conditional-form-mapping-$playVersion"      % "3.1.0",
    "uk.gov.hmrc" %% s"bootstrap-frontend-$playVersion"                 % bootStrapVersion
  )

  val test: Seq[ModuleID]    = Seq(
    "uk.gov.hmrc"         %% s"bootstrap-test-$playVersion" % bootStrapVersion,
    "org.scalatest"       %% "scalatest"                    % "3.2.19",
    "org.playframework"   %% "play-test"                    % "3.0.5",
    "org.jsoup"            % "jsoup"                        % "1.18.1",
    "org.scalatestplus"   %% "scalacheck-1-18"              % "3.2.19.0",
    "org.scalatestplus"   %% "mockito-5-12"                 % "3.2.19.0",
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test
}
