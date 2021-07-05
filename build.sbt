import play.sbt.routes.RoutesKeys
import scala.sys.process._
import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings.{addTestReportOption}
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings

val appName         = "feedback-frontend"
val silencerVersion = "1.7.0"

val migrate: TaskKey[Unit] = taskKey[Unit]("Execute migrate script")

lazy val microservice = Project(appName, file("."))
  .enablePlugins(PlayScala, SbtDistributablesPlugin)
  .settings(publishingSettings: _*)
  .settings(majorVersion := 0)
  .settings(migrate := "./migrate.sh".!)
  .settings(
    scalaVersion := "2.12.12",
    scalacOptions += "-P:silencer:lineContentFilters=^\\w",
    scalacOptions += "-P:silencer:pathFilters=routes;views",
    libraryDependencies ++= Seq(
      compilerPlugin("com.github.ghik" % "silencer-plugin" % silencerVersion cross CrossVersion.full),
      "com.github.ghik" % "silencer-lib" % silencerVersion % Provided cross CrossVersion.full
    ),
    RoutesKeys.routesImport ++= Seq("models._", "config.Binders._"),
    PlayKeys.playDefaultPort := 9514,
    ScoverageKeys.coverageMinimum := 80,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true,
    ScoverageKeys.coverageExcludedFiles := "<empty>;Reverse.*;.*filters.*;.*handlers.*;.*components.*;.*repositories.*;" +
      ".*BuildInfo.*;.*javascript.*;.*FrontendAuditConnector.*;.*Routes.*;.*GuiceInjector;" +
      ".*ControllerConfiguration;.*LanguageSwitchController",
    libraryDependencies ++= AppDependencies(),
    retrieveManaged := true,
    evictionWarningOptions in update :=
      EvictionWarningOptions.default.withWarnScalaVersionEviction(false),
    fork in Test := true,
    resolvers ++= Seq(
      Resolver.jcenterRepo
    ),
    Concat.groups := Seq(
      "javascripts/feedbackfrontend-app.js" ->
        group(Seq("javascripts/feedbackfrontend.js"))
    ),
    uglifyCompressOptions := Seq("unused=false", "dead_code=false"),
    pipelineStages := Seq(digest),
    pipelineStages in Assets := Seq(concat, uglify),
    includeFilter in uglify := GlobFilter("feedbackfrontend-*.js")
  )
