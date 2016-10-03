
enablePlugins(JavaServerAppPackaging)

enablePlugins(RpmPlugin)

name := "s3ingest"

version := "1.0"

scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature",
  "-Xfatal-warnings", "-Xlint:private-shadow", "-unchecked",
  "-language:reflectiveCalls", "-language:postfixOps", "-language:implicitConversions",
  "-Ywarn-unused-import")

val Versions = new {
  val Camel = "2.17.3"
  val Aws = "1.10.77"
}

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.1",

  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "org.slf4j" % "jcl-over-slf4j" % "1.7.21",


  "org.apache.camel" % "camel-core" % Versions.Camel,
  "org.apache.camel" % "camel-aws" % Versions.Camel exclude("com.amazonaws", "aws-java-sdk"),
  "com.amazonaws" % "aws-java-sdk-s3" % Versions.Aws,
  "com.amazonaws" % "aws-java-sdk-cloudfront" % Versions.Aws,
  "com.amazonaws" % "aws-java-sdk-kinesis" % Versions.Aws
  /*
    ,"io.kamon" %% "kamon-core" % "0.6.2",
    "io.kamon" %% "kamon-system-metrics" % "0.6.2",
    "io.kamon" %% "kamon-jmx" % "0.6.2"*/
).map(_.excludeAll(
  ExclusionRule("log4j", "log4j"),
  ExclusionRule("org.apache.logging.log4j", "log4j-core"),
  ExclusionRule("commons-logging", "commons-logging")))