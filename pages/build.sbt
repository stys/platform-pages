name := "platform-pages"

Common.settings

lazy val api = (project in file("modules/api"))
    .enablePlugins(PlayJava)

lazy val repository = (project in file("modules/repository"))
    .enablePlugins(PlayJava, PlayEbean, PlayEnhancer)
    .dependsOn(api)

lazy val markdown = (project in file("modules/markdown"))
    .enablePlugins(PlayJava)
    .dependsOn(api)

lazy val modules = (project in file("modules/modules"))
    .enablePlugins(PlayJava)
    .dependsOn(api, repository, markdown)

lazy val test_app = (project in file("modules/test_app"))
    .enablePlugins(PlayJava)
    .dependsOn(modules)
    .settings(aggregateReverseRoutes := Seq(api))

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)
    .aggregate(api, repository, markdown, modules, test_app)
