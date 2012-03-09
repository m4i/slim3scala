package org.slim3scala.sbt

trait Slim3ScalaBase extends Slim3Base {
  val slim3ScalaVersion               = "0.1.0"
  val slim3ScalaReleaseRepositoryUrl  = "http://repo.slim3scala.org/releases/"
  val slim3ScalaSnapshotRepositoryUrl = "http://repo.slim3scala.org/snapshots/"

  val runtaskConfiguration = config("runtask") hide

  lazy val slim3ScalaRepository =
    if (isSnapshot(slim3ScalaVersion))
      "Slim3Scala Snapshot Repository" at slim3ScalaSnapshotRepositoryUrl
    else
      "Slim3Scala Release Repository" at slim3ScalaReleaseRepositoryUrl

  lazy val slim3ScalaDependency =
    "org.slim3scala" % "slim3scala" % slim3ScalaVersion intransitive
  lazy val slim3ScalaAptDependency =
    "org.slim3scala" % "slim3scala-apt" % slim3ScalaVersion % "runtask"

  override def ivyXML =
    <dependencies>
      <dependency org="org.slim3scala" name="slim3scala-gen"
          rev={ slim3ScalaVersion } conf="runtask">
        <exclude org="org.slim3"/>
      </dependency>
    </dependencies>

  lazy val slim3ScalaJar    = dependencyJar(slim3ScalaDependency)
  lazy val slim3ScalaGenJar =
    dependencyJar("slim3scala-gen", slim3ScalaVersion, "runtask")
  lazy val slim3ScalaAptJar =
    dependencyJar("slim3scala-apt", slim3ScalaVersion, "runtask")

  lazy val s3ViewPath       = mainSourcePath / Constants.VIEW_PACKAGE
  lazy val s3ViewPathFinder =
    descendents(s3ViewPath, "*").filter(!_.isDirectory)

  override def watchPaths = super.watchPaths +++ s3ViewPathFinder

  val scalatestVersion = "1.2"
  lazy val scalatestDependency =
    "org.scalatest" % "scalatest" % scalatestVersion % "test"

  /**
   * Even if there is not a scala source, depends on scala-library.jar
   */
  override protected def prepareWebappAction = prepareWebappTask(
    webappResources, temporaryWarPath, webappClasspath,
    mainDependencies.scalaJars +++ buildLibraryJar, webappUnmanaged
  ) dependsOn(compile, copyResources)
}
