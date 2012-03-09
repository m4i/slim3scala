import sbt._

import java.io.File

import org.slim3scala.sbt.AppEnginePath
import org.slim3scala.sbt.Utils

class Slim3ScalaProject(info: ProjectInfo)
  extends ParentProject(info) with Utils {

  val slim3Version               = "r1392"
  val slim3ReleaseRepositoryUrl  = "https://www.seasar.org/maven/maven2/"
  val slim3SnapshotRepositoryUrl = "http://repo.slim3scala.org/snapshots/"

  val slim3Repository =
    if (isSnapshot(slim3Version))
      "Slim3 Snapshot Repository" at slim3SnapshotRepositoryUrl
    else
      "Slim3 Release Repository" at slim3ReleaseRepositoryUrl

  val scalateVersion   = "1.3.2"
  val scalatestVersion = "1.2"

  val javaToolsJar = (
    Path.fileProperty("java.home") / "lib" / "tools.jar" +++
    Path.fromFile(System.getProperty("java.home") + "/../lib/tools.jar")
  ).get.toList.head

  val publishTo = Resolver.file("repo.slim3scala.org",
    new File("/mnt/repo.slim3scala.org/" +
      (if (isSnapshot(version)) "snapshots" else "releases")))

  override def managedStyle = ManagedStyle.Maven

  override def deliverProjectDependencies = Nil

  lazy val slim3ScalaProject = project(
    "slim3scala",
    "slim3scala",
    new DefaultProject(_) with AppEnginePath {
      override protected def disableCrossPaths = true

      val slim3Dependency =
        "org.slim3" % "slim3" % slim3Version intransitive
      val scalateDependency =
        "org.fusesource.scalate" % "scalate-core" % scalateVersion % "optional"
      val slf4jImplDependency =
        "org.slf4j" % "slf4j-nop" % "1.6.1" % "optional"
      val scalatestDependency =
        "org.scalatest" % "scalatest" % scalatestVersion % "optional"
    }
  )

  lazy val slim3ScalaGenProject = project(
    "slim3scala-gen",
    "slim3scala-gen",
    new DefaultProject(_) {
      override protected def disableCrossPaths = true

      val slim3GenDependency =
        "org.slim3" % "slim3-gen" % slim3Version
      val scalateDependency =
        "org.fusesource.scalate" % "scalate-core" % scalateVersion
      val slf4jImplDependency =
        "ch.qos.logback" % "logback-classic" % "0.9.26" % "runtime"
      val scalatestDependency =
        "org.scalatest" % "scalatest" % scalatestVersion % "test"

      override def compileClasspath = super.compileClasspath +++ javaToolsJar
    }
  )

  lazy val slim3ScalaAptProject = project(
    "slim3scala-apt",
    "slim3scala-apt",
    new DefaultProject(_) {
      override protected def disableCrossPaths = true

      val commonsCliDependency = "commons-cli" % "commons-cli" % "1.2"

      override def compileClasspath = super.compileClasspath +++ javaToolsJar
    }
  )

  lazy val slim3ScalaSbtProject = project(
    "slim3scala-sbt",
    "slim3scala-sbt",
    new PluginProject(_) {
      val slim3GenDependency = "org.slim3" % "slim3-gen" % slim3Version
    }
  )
}
