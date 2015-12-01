name := """ean-module"""

version := "1.0-SNAPSHOT"

organization := "com.example"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test,
  "net.sf.barcode4j" % "barcode4j" % "2.1"
)

// Local file
publishTo := Some(Resolver.file("file",  new File("/Users/username/repo/path")) )

// Url
publishTo := Some(
    "My resolver" at "https://mycompany.com/repo"
)
credentials += Credentials(
    "Repository Realm", "https://mycompany.com/repo", "username", "hashedpassword"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
