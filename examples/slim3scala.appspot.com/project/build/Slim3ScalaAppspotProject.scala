import sbt._

import org.slim3scala.sbt.Slim3ScalaProject
import org.slim3scala.sbt.Scalate

class Slim3ScalaAppspotProject(info: ProjectInfo)
  extends Slim3ScalaProject(info) with Scalate {

  override protected def disableCrossPaths = true
}
