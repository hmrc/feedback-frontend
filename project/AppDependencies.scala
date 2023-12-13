import sbt.*

object AppDependencies {

  val playFrameworkVersionSuffix = "play-28"
  val bootStrapVersion     = "7.19.0"

  val compile: Seq[ModuleID] = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc" %% "play-frontend-hmrc"                        % s"7.2.0-$playFrameworkVersionSuffix",
    "uk.gov.hmrc" %% "play-conditional-form-mapping"             % s"1.13.0-$playFrameworkVersionSuffix",
    "uk.gov.hmrc" %% s"bootstrap-frontend-$playFrameworkVersionSuffix" % bootStrapVersion
  )

  val test: Seq[ModuleID]    = Seq(
    "uk.gov.hmrc"         %% s"bootstrap-test-$playFrameworkVersionSuffix" % bootStrapVersion,
    "org.scalatest"       %% "scalatest"                             % "3.2.16",
    "org.jsoup"            % "jsoup"                                 % "1.16.1",
    "com.typesafe.play"   %% "play-test"                             % "2.9.0-M6",
    "org.scalatestplus"   %% "scalatestplus-scalacheck"              % "3.1.0.0-RC2",
    "org.scalatestplus"   %% "scalatestplus-mockito"                 % "1.0.0-M2",
    "com.vladsch.flexmark" % "flexmark-all"                          % "0.64.2" // Any version above this cause the accessibility tests to fail because it fetches com.ibm.icu::icu4j version 72.1
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test
}
