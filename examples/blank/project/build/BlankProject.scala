import sbt._

import org.slim3scala.sbt.Slim3ScalaProject
//import org.slim3scala.sbt.Scalate

class BlankProject(info: ProjectInfo)
  extends Slim3ScalaProject(info) {
  //extends Slim3ScalaProject(info) with Scalate {

  override protected def disableCrossPaths = true

  //override val s3ScalaControllerSuperclass =
  //  s3RootPackage + ".controller.ApplicationController"

  //override val s3ScalaTemplateSuperclass =
  //  s3RootPackage + ".view.ApplicationTemplate"

  //override lazy val scalateTemplateType = "ssp"
}
