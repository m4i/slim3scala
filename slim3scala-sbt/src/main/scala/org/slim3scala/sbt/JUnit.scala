package org.slim3scala.sbt

import _root_.sbt.DefaultWebProject

trait JUnit extends DefaultWebProject {
  val junitVersion          = "4.8.2"
  val junitInterfaceVersion = "0.5"

  lazy val junitDependency =
    "junit" % "junit" % junitVersion % "test"
  lazy val junitInterfaceDependency =
    "com.novocode" % "junit-interface" % junitInterfaceVersion % "test"
}
