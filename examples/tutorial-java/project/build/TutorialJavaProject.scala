import sbt._

import org.slim3scala.sbt.Slim3Project

class TutorialJavaProject(info: ProjectInfo) extends Slim3Project(info) {
  override protected def disableCrossPaths = true
}
