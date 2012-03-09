package org.slim3scala.sbt

import scala.xml.XML

import _root_.sbt.DefaultWebProject

trait Slim3Base extends DefaultWebProject with Utils {
  val slim3Version               = "r1392"
  val slim3ReleaseRepositoryUrl  = "https://www.seasar.org/maven/maven2/"
  val slim3SnapshotRepositoryUrl = "http://repo.slim3scala.org/snapshots/"

  lazy val slim3Repository =
    if (isSnapshot(slim3Version))
      "Slim3 Snapshot Repository" at slim3SnapshotRepositoryUrl
    else
      "Slim3 Release Repository" at slim3ReleaseRepositoryUrl

  lazy val slim3Dependency = "org.slim3" % "slim3" % slim3Version intransitive

  lazy val slim3Jar    = dependencyJar(slim3Dependency)
  lazy val slim3GenJar = pluginsDependencyJar("slim3-gen", slim3Version)

  lazy val webXmlPath = webappPath / "WEB-INF" / "web.xml"

  lazy val s3RootPackage: String = {
    val webApp = XML.loadFile(webXmlPath.absolutePath)
    (webApp \ "context-param")
      .find(c => (c \ "param-name").text == Constants.ROOT_PACKAGE_KEY) match {
        case Some(contextParam) =>
          (contextParam \ "param-value").text
        case _ =>
          error("Cannot specify " + Constants.ROOT_PACKAGE_KEY)
      }
  }

  lazy val s3RootPackageForPath = s3RootPackage.replace('.', '/')

  lazy val generatedSourcePath     = outputPath / sourceDirectoryName
  lazy val generatedMainSourcePath = generatedSourcePath / mainDirectoryName
  lazy val generatedMainJavaSourcePath =
    generatedMainSourcePath / javaDirectoryName
  lazy val generatedMainScalaSourcePath =
    generatedMainSourcePath / scalaDirectoryName

  def generatedMainSourceRoots =
    (generatedMainJavaSourcePath##) +++ (generatedMainScalaSourcePath##)

  override def mainSourceRoots =
    super.mainSourceRoots +++ generatedMainSourceRoots

  override def watchPaths =
    super.watchPaths --- sources(generatedMainSourceRoots)
}
