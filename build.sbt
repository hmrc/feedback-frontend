import play.sbt.routes.RoutesKeys
import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings

lazy val appName: String = "feedback-frontend"

val migrate: TaskKey[Unit] = taskKey[Unit]("Execute migrate script")

lazy val root = (project in file("."))
  .enablePlugins(PlayScala, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(DefaultBuildSettings.scalaSettings *)
  .settings(DefaultBuildSettings.defaultSettings() *)
  .settings(majorVersion := 0)
  .settings(migrate := "./migrate.sh")
  .settings(
    name := appName,
    scalaVersion := "2.13.12",
    RoutesKeys.routesImport ++= Seq("models._", "config.Binders._"),
    PlayKeys.playDefaultPort := 9514,
    ScoverageKeys.coverageExcludedFiles := "<empty>;Reverse.*;.*filters.*;.*handlers.*;.*components.*;.*repositories.*;" +
      ".*BuildInfo.*;.*javascript.*;.*FrontendAuditConnector.*;.*Routes.*;.*GuiceInjector;" +
      ".*ControllerConfiguration;.*LanguageSwitchController",
    ScoverageKeys.coverageMinimumStmtTotal := 80,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true,
    libraryDependencies ++= AppDependencies(),
    libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always,
    TwirlKeys.templateImports ++= Seq(
      "uk.gov.hmrc.hmrcfrontend.views.html.components._",
      "uk.gov.hmrc.hmrcfrontend.views.html.helpers._"
    ),
    retrieveManaged := true,
    scalafmtOnCompile := false,
    Global / excludeLintKeys += update / evictionWarningOptions,
    update / evictionWarningOptions := EvictionWarningOptions.default.withWarnScalaVersionEviction(false),
    Test / fork := true,
    resolvers ++= Seq(
      Resolver.jcenterRepo
    ),
    // prevent removal of unused code which generates warning errors due to use of third-party libs
    uglifyCompressOptions := Seq("unused=false", "dead_code=false"),
    pipelineStages := Seq(digest),
    // below line required to force asset pipeline to operate in dev rather than only prod
    Assets / pipelineStages := Seq(concat, uglify),
    // only compress files generated by concat
    uglify / includeFilter := GlobFilter("feedbackfrontend-*.js"),
    scalacOptions := Seq(
      "-Wconf:src=routes/.*:s",
      "-Wconf:cat=unused-imports&src=html/.*:s",
      "-Xfatal-warnings",
      "-deprecation"
    )
  )

addCommandAlias("scalafmtAll", "all scalafmtSbt scalafmt test:scalafmt it:scalafmt")
addCommandAlias("testAll", ";coverage ;test ;it:test ;coverageReport")
addCommandAlias("testAllWithScalafmt", ";scalafmtAll ;testAll")
