name := "test-app"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache
)     

play.Project.playJavaSettings

lazy val pages = project.in(file("./module"))

lazy val main = project.in(file("."))
	.dependsOn(pages)
	.aggregates(pages)