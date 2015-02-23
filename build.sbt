name := "replicate"

organization := "com.nitindhar"

version := "0.0.1"

scalaVersion := "2.11.5"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature")

resolvers += Resolver.typesafeRepo("releases")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.9",
  "org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23"
)
