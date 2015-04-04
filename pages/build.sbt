name := "platform-pages"

organization := "com.stys"

version := "1.1.1"

scalaVersion := "2.11.2"

crossScalaVersions := Seq("2.10.4", "2.11.2")

libraryDependencies ++= Seq(
  "org.commonjava.googlecode.markdown4j" % "markdown4j" % "2.2-cj-1.0",
  javaEbean
)     

lazy val root = (project in file(".")).enablePlugins(PlayJava)