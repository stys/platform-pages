import sbt._
import Keys._
import play.sbt.routes.RoutesKeys._

object Common {
  val settings: Seq[Setting[_]] = Seq(
    organization := "com.stys",
    version := "1.2.0",
    scalaVersion := "2.11.7",
    routesGenerator := InjectedRoutesGenerator
  )
}