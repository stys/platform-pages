name := "platform-pages-repository"

libraryDependencies += evolutions

playEbeanModels in Compile := Seq("com.stys.platform.pages.repository.models.*")

