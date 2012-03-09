import sbt._

class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
  val slim3ScalaVersion       = "0.1.0"
  val slim3ScalaRepository    = "Slim3Scala Repository" at "http://repo.slim3scala.org/releases/"
  val slim3ScalaSbtDependency = "org.slim3scala" % "slim3scala-sbt" % slim3ScalaVersion
}
