package org.slim3scala.sbt

import _root_.sbt.ProjectInfo

abstract class Slim3Project(info: ProjectInfo)
  extends AppEngineProject(info)
  with Slim3AntTask
  with JUnit
