package org.slim3scala.sbt

import _root_.sbt.DefaultWebProject
import _root_.sbt.ProjectInfo

abstract class AppEngineProject(info: ProjectInfo)
  extends DefaultWebProject(info)
  with AppEngineDevAppserver
