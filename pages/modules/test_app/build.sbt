name := "platform-pages-test-app"

libraryDependencies ++= Seq(
    "org.webjars" % "bootstrap" % "3.3.7",
    evolutions,
    javaJdbc
)

publishArtifact := false