package org.slim3scala.sbt

import scala.xml.Elem
import scala.xml.NodeSeq
import scala.xml.Null
import scala.xml.UnprefixedAttribute

trait Scalate extends Slim3ScalaAntTask {
  val scalateVersion = "1.3.2"
  lazy val scalateDependency =
    "org.fusesource.scalate" % "scalate-core" % scalateVersion
  lazy val slf4jImplDependency =
    "org.slf4j" % "slf4j-nop" % "1.6.1" % "runtime" intransitive

  //val scalateTemplateType = "scaml"    // >= 2.8.0
  lazy val scalateTemplateType = "scaml" // <  2.8.0

  lazy val scalateViewSuffix =
    Constants.VIEW_SUFFIX + "." + scalateTemplateType

  override def s3AntBuildXMLTasks: Map[String, String] =
    super.s3AntBuildXMLTasks ++ Map(
      "gen-scalate-view"       -> "org.slim3scala.gen.task.GenScalateViewTask",
      "generate-scalate-class" -> "org.slim3scala.gen.task.GenerateScalateClassTask")

  override def s3AntBuildXMLTargets: Map[String, NodeSeq] = Map((
    for ((name, child) <- super.s3AntBuildXMLTargets)
      yield name -> NodeSeq.fromSeq(name match {
        case "gen-controller" =>
          child(_.label == "input") ++
          child(_.label == "gen-controller").map {
            _.asInstanceOf[Elem] %
              new UnprefixedAttribute("viewSuffix", scalateViewSuffix, Null)
          } ++
          <gen-scalate-view wardir="${warDir}" controllerPath="${controllerPath}" viewdir="${viewDir}" scalateTemplateType={ scalateTemplateType }/>

        case "generate-template-class" =>
          child ++
          <generate-scalate-class wardir="${warDir}" srcdir="${viewSrcDir}" viewdir="${viewDir}"/>

        case _ =>
          child
      })
  ).toSeq: _*)
}
