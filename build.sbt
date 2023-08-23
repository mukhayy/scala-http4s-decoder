import sbt.Keys.libraryDependencies

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

val http4sVersion = "0.22.11"
lazy val root = (project in file("."))
  .settings(
    name := "scala-http4s-decoder",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-ember-client" % http4sVersion,
      "org.http4s" %% "http4s-dsl"          % http4sVersion,
      "org.scalatestplus" %% "mockito-3-4" % "3.2.10.0" % Test,
      "org.scalatest" %% "scalatest" % "3.2.15" % Test,
      "com.github.tomakehurst" % "wiremock" % "2.27.2" % Test,
      "org.scalactic" %% "scalactic" % "3.2.16",
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "io.circe" %% "circe-generic" % "0.14.1",
      "io.circe" %% "circe-literal" % "0.14.1",
      "io.circe" %% "circe-parser" % "0.14.1",
    )
  )





