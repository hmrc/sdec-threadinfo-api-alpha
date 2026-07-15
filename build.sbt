import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings

val appName = "sdec-threadinfo-api-alpha"

ThisBuild / majorVersion := 0

lazy val compilerSettings = Seq(
  scalaVersion := "3.3.7",
  scalacOptions += "-Wconf:src=routes/.*:s",
  scalacOptions += "-Wconf:msg=unused import&src=html/.*:s"
)

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .disablePlugins(
    JUnitXmlReportPlugin
  )
  .settings(
    Compile / scalafmtOnCompile := true,
    Test / scalafmtOnCompile    := true,
    PlayKeys.playDefaultPort    := 4001,
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
    compilerSettings
  )
  .settings(
    Compile / unmanagedResourceDirectories += baseDirectory.value / "resources",
    Test / unmanagedSourceDirectories := (Test / baseDirectory)(base =>
      Seq(base / "test", base / "test-common")
    ).value,
    Test / unmanagedResourceDirectories := Seq(
      baseDirectory.value / "test-resources"
    )
  )
  .settings(CodeCoverageSettings.settings: _*)

lazy val it = project
  .enablePlugins(PlayScala)
  .dependsOn(microservice % "test->test")
  .settings(
    DefaultBuildSettings.itSettings(),
    compilerSettings
  )
  .settings(libraryDependencies ++= AppDependencies.it)

inThisBuild(
  List(
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision
  )
)

addCommandAlias(
  "prePrChecks",
  "; scalafmtCheckAll; scalafmtSbtCheck; scalafixAll --check"
)
addCommandAlias(
  "checkCodeCoverage",
  "; clean; coverage; test; it/test; coverageReport"
)
addCommandAlias("lint", "; scalafmtAll; scalafmtSbt; scalafixAll")
addCommandAlias("prePush", "; reload; clean; compile; test; it/test; lint;")
