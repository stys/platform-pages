name := "platform-pages"

organization := "com.stys"

version := "1.0.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.commonjava.googlecode.markdown4j" % "markdown4j" % "2.2-cj-1.0",
  javaEbean
)     

play.Project.playJavaSettings
