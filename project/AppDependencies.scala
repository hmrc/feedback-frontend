import sbt._
import play.core.PlayVersion

object AppDependencies {

  val compile: Seq[ModuleID] = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc" %% "play-frontend-hmrc"             % "6.7.0-play-28",
    "uk.gov.hmrc" %% "play-conditional-form-mapping"  % "1.12.0-play-28",
    "uk.gov.hmrc" %% "bootstrap-frontend-play-28"     % "7.8.0"
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"            %% "bootstrap-test-play-28"    % "7.8.0",
    "org.scalatest"          %% "scalatest"                 % "3.2.9",
    "org.jsoup"              %  "jsoup"                     % "1.14.3",
    "com.typesafe.play"      %% "play-test"                 % PlayVersion.current,
    "org.scalatestplus"      %% "scalatestplus-scalacheck"  % "3.1.0.0-RC2",
    "org.scalatestplus"      %% "scalatestplus-mockito"     % "1.0.0-M2",
    "com.vladsch.flexmark"   %  "flexmark-all"              % "0.36.8"
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test
}
