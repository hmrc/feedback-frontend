import sbt._
import play.core.PlayVersion

object AppDependencies {

  val playVersion = "play-28"

  val compile = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc" %% "play-frontend-hmrc"               % s"3.21.0-$playVersion",
    "uk.gov.hmrc" %% "play-conditional-form-mapping"    % s"1.11.0-$playVersion",
    "uk.gov.hmrc" %% s"bootstrap-frontend-$playVersion" % "5.12.0"
  )

  val test = Seq(
    "org.scalatest"          %% "scalatest"          % "3.2.9",
    "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0",
    "org.jsoup"              % "jsoup"               % "1.10.3",
    "com.typesafe.play"      %% "play-test"          % PlayVersion.current,
    "org.scalatestplus"      %% "scalatestplus-scalacheck" % "3.1.0.0-RC2",
    "org.scalatestplus"      %% "scalatestplus-mockito"    % "1.0.0-M2",
    "com.vladsch.flexmark"   % "flexmark-all"       % "0.35.10"
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test
}
