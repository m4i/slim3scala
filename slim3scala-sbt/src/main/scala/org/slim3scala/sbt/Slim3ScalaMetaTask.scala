package org.slim3scala.sbt

import _root_.sbt.FileUtilities
import _root_.sbt.Path
import _root_.sbt.PathFinder

trait Slim3ScalaMetaTask extends Slim3ScalaBase with AppEnginePath {
  lazy val s3ScalaRootPath =
    Path.fromString(mainScalaSourcePath, s3RootPackageForPath)
  lazy val s3ScalaGeneratedRootPath =
    Path.fromString(generatedMainScalaSourcePath, s3RootPackageForPath)

  lazy val s3ScalaModelPath = s3ScalaRootPath / Constants.MODEL_PACKAGE
  lazy val s3ScalaMetaPath  = s3ScalaGeneratedRootPath / Constants.META_PACKAGE

  lazy val s3ScalaSourceRootsForMeta: PathFinder = s3ScalaModelPath
  lazy val s3ScalaSourcesForMeta: PathFinder     = sources(s3ScalaSourceRootsForMeta)

  lazy val javaToolsJar = (
    Path.fileProperty("java.home") / "lib" / "tools.jar" +++
    Path.fromFile(System.getProperty("java.home") + "/../lib/tools.jar")
  ).get.toList.head

  lazy val commonsCliJar = dependencyJar("commons-cli", "runtask")

  lazy val generateMeta = generateMetaAction
  def generateMetaAction = fileTask(s3ScalaMetaPath from s3ScalaSourcesForMeta) {
    val sources = s3ScalaSourcesForMeta.getPaths

    if (sources.isEmpty) {
      None

    } else {
      val jvmClasspath =
        javaToolsJar :: commonsCliJar :: slim3ScalaAptJar ::
        slim3Jar :: slim3GenJar :: slim3ScalaGenJar :: Nil

      val aptClasspath = (buildLibraryJar +++ compileClasspath).get

      defaultRunner.run(
        "org.slim3scala.apt.Main",
        jvmClasspath,
        "-cp" :: Path.makeString(aptClasspath) ::
        "-s" :: generatedMainScalaSourcePath.absolutePath ::
        "-factory" :: "org.slim3scala.gen.processor.ModelProcessorFactory" ::
        "-nocompile" ::
        sources.toList,
        log
      ) match {
        case error: Some[_] =>
          error
        case _ =>
          if (!s3ScalaMetaPath.asFile.mkdirs()) {
            FileUtilities.touch(s3ScalaMetaPath, log)
          }
          None
      }
    }
  } describedAs "Generates Slim3 meta in Scala."

  override def compileAction = super.compileAction dependsOn generateMeta
}
