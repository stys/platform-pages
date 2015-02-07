# Extensible wiki module for Play 2.3.x

Pages is a simple and extensible **wiki** module for Play Framework (Java) applications. 

## Usage

### Setup dependencies

Add resolver and dependency to `build.sbt` 

'''
resolvers += "Platform releases" at "https://raw.github.com/stys/maven-releases/master/"

libraryDependencies ++= Seq(
  ...
  "com.stys" %% "platform-pages" % "1.1.0",
  javaCore,
  javaJdbc,
  javaEbean
)     
'''


