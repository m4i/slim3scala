package org.slim3scala.sbt

import _root_.sbt.Path

class EnvPath(source: String) extends EnvString(source) {
  def toPath = Path.fromFile(string)
}

object EnvPath {
  implicit def toPath(envPath: EnvPath) = envPath.toPath
}
