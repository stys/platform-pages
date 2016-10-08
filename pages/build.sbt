name := "platform-pages"

organization := "com.stys"

version := "1.2.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.commonjava.googlecode.markdown4j" % "markdown4j" % "2.2-cj-1.0",
  evolutions
)     

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)