import sbt._

import org.slim3scala.sbt.Slim3ScalaProject

class TutorialScalaProject(info: ProjectInfo) extends Slim3ScalaProject(info) {
  override protected def disableCrossPaths = true
}
