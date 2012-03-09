import sbt._

import org.slim3scala.sbt.Slim3ScalaProject
import org.slim3scala.sbt.Scalate

class TutorialScalateProject(info: ProjectInfo)
  extends Slim3ScalaProject(info) with Scalate {

  override protected def disableCrossPaths = true

  lazy val testAndPrepareWebapp = prepareWebappAction dependsOn test
}
