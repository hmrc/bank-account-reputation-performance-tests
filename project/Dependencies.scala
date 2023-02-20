import sbt._

object Dependencies {

  val test = Seq(
    "com.typesafe" % "config" % "1.4.1" % Test,
    "uk.gov.hmrc" %% "performance-test-runner" % "5.+" % Test,
    "io.gatling" % "gatling-test-framework" % "3.6.1" % Test,
    "io.gatling.highcharts" % "gatling-charts-highcharts" % "3.6.1" % Test
  )
}
