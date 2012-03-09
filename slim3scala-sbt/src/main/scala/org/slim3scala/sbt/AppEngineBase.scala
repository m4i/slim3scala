package org.slim3scala.sbt

import _root_.sbt.DefaultWebProject

trait AppEngineBase extends DefaultWebProject with AppEnginePath {
  val gaeLibAgentPath = gaeLibPath / "agent"

  val gaeToolsJar = gaeLibPath / "appengine-tools-api.jar"
  val gaeAgentJar = gaeLibAgentPath / "appengine-agent.jar"

  override def webappUnmanaged =
    temporaryWarPath / "WEB-INF" / "appengine-generated" ***
}
