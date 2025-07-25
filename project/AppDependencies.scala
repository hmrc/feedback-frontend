import sbt.*

object AppDependencies {

  val playVersion = "play-30"
  val bootStrapVersion = "9.18.0"

  val compile: Seq[ModuleID] = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc" %% s"play-frontend-hmrc-$playVersion"                 % "12.8.0",
    "uk.gov.hmrc" %% s"play-conditional-form-mapping-$playVersion"      % "3.3.0",
    "uk.gov.hmrc" %% s"bootstrap-frontend-$playVersion"                 % bootStrapVersion
  )

  val test: Seq[ModuleID]    = Seq(
    "uk.gov.hmrc"         %% s"bootstrap-test-$playVersion" % bootStrapVersion,
    "org.jsoup"            % "jsoup"                        % "1.19.1",
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test
}
