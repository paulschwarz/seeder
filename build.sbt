lazy val commonSettings = Seq(
  version in ThisBuild := "0.0.2",
  organization in ThisBuild := "me.paulschwarz"
)

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    sbtPlugin := true,
    name := "seeder",
    description := "A helper for creating and seeding – ideal for testing",
    licenses += ("MIT", url("https://opensource.org/licenses/MIT")),
    publishMavenStyle := false,
    bintrayRepository := "sbt-plugins",
    bintrayOrganization in bintray := Some("paulschwarz"),
    publishMavenStyle := true,
    pomIncludeRepository := { _ ⇒ false }
  )

libraryDependencies += "junit" % "junit" % "4.12"
libraryDependencies += "org.mockito" % "mockito-core" % "2.15.0"
libraryDependencies += "com.github.javafaker" % "javafaker" % "0.14"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
