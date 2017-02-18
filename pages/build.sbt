name := "platform-pages"

lazy val commonSettings = Seq(
  organization := "com.stys",
  version := "1.2.0",
  scalaVersion := "2.11.7",
  routesGenerator := InjectedRoutesGenerator,
  publishTo := Some(Resolver.file("file", new File("/Users/stys/Work/maven-releases")))
)

lazy val api = (project in file("modules/api"))
    .settings(commonSettings)
    .enablePlugins(PlayJava)

lazy val repository = (project in file("modules/repository"))
    .settings(commonSettings)
    .enablePlugins(PlayJava, PlayEbean, PlayEnhancer)
    .dependsOn(api)

lazy val markdown = (project in file("modules/markdown"))
    .settings(commonSettings)
    .enablePlugins(PlayJava)
    .dependsOn(api)

lazy val modules = (project in file("modules/modules"))
    .settings(commonSettings)
    .enablePlugins(PlayJava)
    .dependsOn(api, repository, markdown)

lazy val test_app = (project in file("modules/test_app"))
    .enablePlugins(PlayJava)
    .dependsOn(modules)
    .settings(
      commonSettings,
      aggregateReverseRoutes := Seq(api)
    )

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)
    .settings(commonSettings)
    .aggregate(api, repository, markdown, modules, test_app)
