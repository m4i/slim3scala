package org.slim3scala.sbt

import java.io.FileWriter
import scala.xml.Elem
import scala.xml.NodeSeq
import scala.xml.PrettyPrinter

import _root_.sbt.Path

trait Slim3ScalaAntTask extends Slim3ScalaBase {
  val s3ScalaControllerSuperclass = ClassConstants.Controller
  val s3ScalaTemplateSuperclass   = ClassConstants.Template

  lazy val antJar         = pluginsDependencyJar("ant")
  lazy val antLauncherJar = pluginsDependencyJar("ant-launcher")

  lazy val scalateJars = List(
    dependencyJar("scalate-core", "runtask"),
    dependencyJar("scalate-util", "runtask"),
    dependencyJar("slf4j-api",    "runtask"))
  lazy val slf4jImplJars = List(
    dependencyJar("logback-classic", "runtask"),
    dependencyJar("logback-core",    "runtask"))

  lazy val s3RuntaskPath           = outputPath / "runtask"
  lazy val s3AntBuildXMLPath       = s3RuntaskPath / "build.xml"
  lazy val s3RuntaskLogbackXMLPath = s3RuntaskPath / "logback.xml"

  val s3RuntaskLogbackLevel = "info"

  var initializedAnt = false

  def antTask(target: String) =
    initializeAnt && runTask(
      Some("org.apache.tools.ant.Main"),
      antJar +++ antLauncherJar,
      "-buildfile" :: s3AntBuildXMLPath.absolutePath :: target :: Nil)

  def initializeAnt = task {
    if (!initializedAnt || !s3AntBuildXMLPath.exists) {
      initializedAnt = true
      createXML(s3AntBuildXMLPath, s3AntBuildXML)
      createXML(s3RuntaskLogbackXMLPath, s3RuntaskLogbackXML)
    }
    None
  }

  def createXML(path: Path, elem: Elem) {
    path.asFile.getParentFile.mkdirs()
    val xml = new PrettyPrinter(256, 2).format(elem)
    var writer: FileWriter = null
    try {
      writer = new FileWriter(path.asFile)
      writer.write(xml)
    } finally {
      if (writer != null) {
        writer.close()
      }
    }
  }

  lazy val genController =
    antTask("gen-controller") describedAs
      "Generates Slim3 controller in Scala."

  lazy val genControllerWithoutView =
    antTask("gen-controller-without-view") describedAs
      "Generates Slim3 controller without view in Scala."

  lazy val genModel =
    antTask("gen-model") describedAs
      "Generates Slim3 model in Scala."

  lazy val genService =
    antTask("gen-service") describedAs
      "Generates Slim3 service in Scala."

  lazy val generateTemplateClass = generateTemplateClassAction
  def generateTemplateClassAction =
    antTask("generate-template-class") describedAs
      "Generate Slim3 template classes in Scala."

  override def compileAction =
    super.compileAction dependsOn generateTemplateClass

  lazy val s3AntBuildXML =
    <project name="slim3scala-gen">
      { for ((n, v) <- s3AntBuildXMLProperties)
          yield <property name={ n } value={ v.absolutePath }/> }
      <path id="classpath">
        { s3AntBuildXMLClasspath.map(j => <path path={ j.absolutePath }/>) }
      </path>
      { for ((n, c) <- s3AntBuildXMLTasks)
          yield <taskdef name={ n } classname={ c } classpathref="classpath"/> }
      { for ((n, c) <- s3AntBuildXMLTargets)
        yield <target name={ n }>{ c }</target> }
    </project>

  def s3AntBuildXMLProperties: Map[String, Path] = Map(
    "warDir"     -> webappPath,
    "srcDir"     -> mainScalaSourcePath,
    "testDir"    -> testScalaSourcePath,
    "viewSrcDir" -> generatedMainScalaSourcePath,
    "viewDir"    -> s3ViewPath)

  def s3AntBuildXMLClasspath: List[Path] =
    buildLibraryJar :: slim3GenJar :: slim3ScalaGenJar ::
    s3RuntaskPath :: scalateJars ::: slf4jImplJars

  def s3AntBuildXMLTasks: Map[String, String] = Map(
    "gen-controller"          -> "org.slim3scala.gen.task.GenControllerTask",
    "gen-model"               -> "org.slim3scala.gen.task.GenModelTask",
    "gen-service"             -> "org.slim3scala.gen.task.GenServiceTask",
    "gen-view"                -> "org.slim3scala.gen.task.GenViewTask",
    "generate-template-class" -> "org.slim3scala.gen.task.GenerateTemplateClassTask")

  def s3AntBuildXMLTargets: Map[String, NodeSeq] = Map(
    "gen-controller" ->
      <input message="Input a controller path." addproperty="controllerPath"/>
      <gen-controller wardir="${warDir}" srcdir="${srcDir}" testdir="${testDir}" controllerpath="${controllerPath}" useView="true" superclassName={ s3ScalaControllerSuperclass }/>
      <gen-view wardir="${warDir}" controllerPath="${controllerPath}" viewdir="${viewDir}"/>
      ,
    "gen-controller-without-view" ->
      <input message="Input a controller path." addproperty="controllerPath"/>
      <gen-controller wardir="${warDir}" srcdir="${srcDir}" testdir="${testDir}" controllerpath="${controllerPath}" useView="false" superclassName={ s3ScalaControllerSuperclass }/>
      ,
    "gen-model" ->
      <input message="Input a model name. (ex.  Foo -> root.model.Foo,  bar.Foo -> root.model.bar.Foo). Sub-model extends Super-model." addproperty="modelDefinition"/>
      <gen-model wardir="${warDir}" srcdir="${srcDir}" testdir="${testDir}" modelDefinition="${modelDefinition}" modelClassNameProperty={ Constants.MODEL_CLASS_NAME_PROPERTY }/>
      ,
    "gen-service" ->
      <input message="Input a service name. (ex.  FooService -> root.service.FooService,  bar.FooService -> root.service.bar.FooService)" addproperty="serviceDefinition"/>
      <gen-service wardir="${warDir}" srcdir="${srcDir}" testdir="${testDir}" serviceDefinition="${serviceDefinition}"/>
      ,
    "generate-template-class" ->
      <generate-template-class wardir="${warDir}" srcdir="${viewSrcDir}" viewdir="${viewDir}" superclassName={ s3ScalaTemplateSuperclass }/>
    )

  lazy val s3RuntaskLogbackXML =
    <configuration>
      <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
          <pattern>%d{{HH:mm:ss.SSS}} [%thread] %-5level %logger{{36}} - %msg%n</pattern>
        </encoder>
      </appender>
      <root level={ s3RuntaskLogbackLevel }>
        <appender-ref ref="STDOUT"/>
      </root>
    </configuration>
}
