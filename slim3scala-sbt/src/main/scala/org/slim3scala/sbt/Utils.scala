package org.slim3scala.sbt

import java.util.regex.Pattern

import _root_.sbt.BasicDependencyProject
import _root_.sbt.ModuleID
import _root_.sbt.Version
import _root_.sbt.{ Path, PathFinder }
import _root_.sbt.{ AllPassFilter, ExactFilter, NameFilter, PatternFilter }
import _root_.sbt.{ Format, SimpleFormat  }

trait Utils extends BasicDependencyProject {
  def findPath(parent: Path, filters: NameFilter*): Path = {
    val pair = (parent.asInstanceOf[PathFinder], parent.relativePath)
    val (finder, finderString) = (pair /: filters) { (pair, filter) =>
      (
        pair._1 * filter,
        pair._2 + "/" + (filter match {
          case AllPassFilter    => "*"
          case f: ExactFilter   => f.matchName
          case f: PatternFilter => f.pattern
          case f =>
            throw new IllegalArgumentException(
              f.getClass.getName + " is not supported.")
        })
      )
    }
    finder.get.toList match {
      case path :: Nil => path
      case Nil         => error("Not found %s.".format(finderString))
      case _           => error("Matches %s too many.".format(finderString))
    }
  }

  def dependencyJar(dependency: ModuleID): Path = {
    dependencyJar(
      dependency.name,
      dependency.revision,
      dependency.configurations.getOrElse("compile"))
  }

  def dependencyJar(
      name: String, revision: String, configuration: String): Path = {
    findPath(
      managedDependencyPath, configuration,
      "%s-%s.jar".format(name, revision))
  }

  def dependencyJar(name: String, configuration: String): Path = {
    findPath(managedDependencyPath, configuration, anyRevisionFilter(name))
  }

  def dependencyJar(name: String): Path = {
    dependencyJar(name, "*")
  }

  def pluginsDependencyJar(name: String, revision: String): Path = {
    findPath(
      info.pluginsManagedDependencyPath,
      "scala_" + defScalaVersion.value,
      "%s-%s.jar".format(name, revision))
  }

  def pluginsDependencyJar(name: String): Path = {
    findPath(
      info.pluginsManagedDependencyPath,
      "scala_" + defScalaVersion.value,
      anyRevisionFilter(name))
  }

  def anyRevisionFilter(name: String): PatternFilter = {
    new PatternFilter(Pattern.compile("""%s-\d\.\d.*\.jar""".format(name)))
  }

  def isSnapshot(version: String): Boolean = {
    version.endsWith("-SNAPSHOT") || version.matches("""r\d+""")
  }

  def isSnapshot(version: Version): Boolean = {
    isSnapshot(version.toString)
  }

  implicit lazy val envStringFormat: Format[EnvString] =
    new SimpleFormat[EnvString] {
      def fromString(s: String) = new EnvString(s)
    }

  implicit lazy val envPathFormat: Format[EnvPath] =
    new SimpleFormat[EnvPath] {
      def fromString(s: String) = new EnvPath(s)
    }
}
