name := "akka-antivirus-ui"

version := "0.1"

scalaVersion := "2.13.6"

val akka = "2.6.14"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed",
  "com.typesafe.akka" %% "akka-remote"
).map(_ % akka)

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"

libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0"

libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.15.0"
libraryDependencies += "com.github.pureconfig" %% "pureconfig-yaml" % "0.15.0"
