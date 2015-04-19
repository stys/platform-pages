# Extensible wiki module for Play 2.3.x

Pages is an extensible **wiki** module for Play Framework (Java) applications. Default implementation uses SQL database storage (via Ebean) and markdown syntax for pages (via markdown4j).  

## Usage

### Setup dependencies

Add resolver and dependency to `build.sbt` 

```sbt
resolvers += "Platform releases" at "https://raw.github.com/stys/maven-releases/master/"

libraryDependencies ++= Seq(
  ...
  "com.stys" %% "platform-pages" % "1.1.2",
  javaCore,
  javaJdbc,
  javaEbean
)     
```


