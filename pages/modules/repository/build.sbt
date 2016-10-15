name := "platform-pages-repository"

Common.settings

libraryDependencies += evolutions

playEbeanModels in Compile := Seq("com.stys.platform.pages.repository.models.*")

