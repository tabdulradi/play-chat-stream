name := """play-chat-stream"""

version := "0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-netty-server" % "2.5.0-M2",
  "com.typesafe.akka" %% "akka-cluster-tools" % "2.4.1",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.4",
  "org.slf4j" % "slf4j-simple" % "1.7.14"
)

scalariformSettings

fork in run := true