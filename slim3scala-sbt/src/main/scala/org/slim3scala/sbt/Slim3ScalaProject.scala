package org.slim3scala.sbt

import _root_.sbt.ProjectInfo

abstract class Slim3ScalaProject(info: ProjectInfo)
  extends AppEngineProject(info)
  with Slim3ScalaAntTask
  with Slim3ScalaMetaTask
