name := "test-app"

version := "1.1.0"

scalaVersion := "2.11.2"

libraryDependencies ++= Seq(
  "com.stys" %% "platform-pages" % "1.1.4",
  javaJdbc,
  javaEbean,
  cache
)     

lazy val root = project.in(file(".")).enablePlugins(PlayJava)