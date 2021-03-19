import play.sbt.PlayImport.guice
import sbt._

object AppDependencies {
  import play.core.PlayVersion

  val playVersion = "play-26"

  val compile = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc" %% "simple-reactivemongo"          % s"8.0.0-$playVersion",
    "uk.gov.hmrc" %% "logback-json-logger"           % "4.8.0",
    "uk.gov.hmrc" %% "govuk-template"                % s"5.65.0-$playVersion",
    "uk.gov.hmrc" %% "play-health"                   % s"3.16.0-$playVersion",
    "uk.gov.hmrc" %% "play-ui"                       % s"9.0.0-$playVersion",
    "uk.gov.hmrc" %% "http-caching-client"           % s"9.3.0-$playVersion",
    "uk.gov.hmrc" %% "play-conditional-form-mapping" % s"1.6.0-$playVersion",
    "uk.gov.hmrc" %% s"bootstrap-$playVersion"       % "3.1.0",
    "uk.gov.hmrc" %% "play-language"                 % s"4.11.0-$playVersion")

  val test = Seq(
    "uk.gov.hmrc"            %% "hmrctest"           % s"3.9.0-$playVersion",
    "org.scalatest"          %% "scalatest"          % "3.0.4",
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.3",
    "org.pegdown"            % "pegdown"             % "1.6.0",
    "org.jsoup"              % "jsoup"               % "1.10.3",
    "com.typesafe.play"      %% "play-test"          % PlayVersion.current,
    "org.mockito"            % "mockito-all"         % "1.10.19",
    "org.scalacheck"         %% "scalacheck"         % "1.13.4"
  ).map(_ % Test)

  def apply() = compile ++ test
}
