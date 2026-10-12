import play.sbt.routes.RoutesKeys
import scoverage.ScoverageKeys

lazy val appName: String = "feedback-frontend"

val migrate: TaskKey[Unit] = taskKey[Unit]("Execute migrate script")

lazy val root = (project in file("."))
  .enablePlugins(PlayScala, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(majorVersion := 0)
  .settings(migrate := "./migrate.sh")
  .settings(
    name := appName,
    scalaVersion := "3.6.4",
    RoutesKeys.routesImport ++= Seq("models._", "config.Binders._"),
    PlayKeys.playDefaultPort := 9514,
    libraryDependencies ++= AppDependencies(),
    libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always,
    TwirlKeys.templateImports ++= Seq(
      "uk.gov.hmrc.govukfrontend.views.html.components._",
      "uk.gov.hmrc.hmrcfrontend.views.html.components._",
      "uk.gov.hmrc.hmrcfrontend.views.html.helpers._"
    ),
    retrieveManaged := true,
    Global / excludeLintKeys += update / evictionWarningOptions,
    update / evictionWarningOptions := EvictionWarningOptions.default.withWarnScalaVersionEviction(false),
    Test / fork := true,
    scalacOptions := Seq(
      "-Wconf:src=routes/.*:s",
      "-Wconf:cat=unused-imports&src=html/.*:s",
      "-Xfatal-warnings",
      "-deprecation"
    ),
    ScoverageKeys.coverageExcludedFiles := "<empty>;Reverse.*;.*handlers.*;.*components.*;" +
      ".*Routes.*;.*viewmodels.govuk.*;",
    ScoverageKeys.coverageMinimumStmtTotal := 89,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true
  )
