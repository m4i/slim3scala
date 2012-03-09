package org.slim3scala.sbt

import _root_.sbt.BasicScalaProject
import _root_.sbt.PathFinder

trait AppEnginePath extends BasicScalaProject with Utils {
  val appengineSdkHome = property[EnvPath]

  val gaeSdkPath = appengineSdkHome.get match {
    case Some(path) =>
      //if (path.toString.trim.nonEmpty && path.asFile.isDirectory) path.toPath // >= 2.8.0
      if (!path.toString.trim.isEmpty && path.asFile.isDirectory) path.toPath   // <  2.8.0
      else error("No such directory - appengine.sdk.home = \"%s\"".format(path))
    case _ => error("You need to set appengine.sdk.home")
  }

  val gaeLibPath       = gaeSdkPath / "lib"
  val gaeLibSharedPath = gaeLibPath / "shared"
  val gaeLibUserPath   = gaeLibPath / "user"

  val gaeServletJar = findPath(gaeLibSharedPath, "geronimo-servlet_*_spec-*.jar")
  val gaeApiJar     = findPath(gaeLibUserPath, "appengine-api-*-sdk-*.jar")
  val gaeApiLabsJar = findPath(gaeLibUserPath, "appengine-api-labs-*.jar")

  val gaeUnmanagedClasspath: PathFinder = gaeApiJar +++ gaeApiLabsJar
  val gaeProvidedClasspath:  PathFinder = gaeServletJar

  override def unmanagedClasspath =
    super.unmanagedClasspath +++ gaeUnmanagedClasspath

  override def providedClasspath =
    super.providedClasspath +++ gaeProvidedClasspath
}
