package org.slim3scala.sbt

import scala.util.DynamicVariable

import _root_.sbt.Path

import org.apache.tools.ant.BuildLogger
import org.apache.tools.ant.DefaultLogger
import org.apache.tools.ant.{ Project => AntProject }
import org.apache.tools.ant.taskdefs.Apt
import org.apache.tools.ant.types.{ Path => AntPath }

import org.slim3.gen.{ ClassConstants => JClassConstants }
import org.slim3.gen.task.GenControllerTask
import org.slim3.gen.task.GenGWTServiceTask
import org.slim3.gen.task.GenGWTServiceImplTask
import org.slim3.gen.task.GenModelTask
import org.slim3.gen.task.GenServiceTask
import org.slim3.gen.task.GenViewTask

trait Slim3AntTask extends Slim3Base with AppEnginePath {
  val s3ControllerSuperclass = JClassConstants.Controller

  val antProject = new DynamicVariable[AntProject](null)

  lazy val antLogger: BuildLogger = {
    val logger = new DefaultLogger
    logger.setMessageOutputLevel(AntProject.MSG_INFO)
    logger.setOutputPrintStream(Console.out)
    logger.setErrorPrintStream(Console.err)
    logger
  }

  def antTask(block: => Unit) = task {
    try {
      antProject.withValue(new AntProject) {
        antProject.value.addBuildListener(antLogger)
        block
      }
      None
    } catch {
      case e => Some(e.getMessage)
    }
  }

  lazy val genJavaController = task { args =>
    if (args.length == 1)
      antTask {
        genJavaControllerTask(args(0), true)
        genJavaViewTask(args(0))
      }
    else
      task { Some("Usage: gen-java-controller <controllerPath>") }
  } describedAs "Generates Slim3 controller in Java."

  lazy val genJavaControllerWithoutView = task { args =>
    if (args.length == 1)
      antTask {
        genJavaControllerTask(args(0), false)
      }
    else
      task { Some("Usage: gen-java-controller-without-view <controllerPath>") }
  } describedAs "Generates Slim3 controller without view in Java."

  lazy val genJavaModel = task { args =>
    if (args.length == 1)
      antTask {
        genJavaModelTask(args(0))
      }
    else
      task { Some("Usage: gen-java-model <modelName>") }
  } describedAs "Generates Slim3 model in Java."

  lazy val genJavaService = task { args =>
    if (args.length == 1)
      antTask {
        genJavaServiceTask(args(0))
      }
    else
      task { Some("Usage: gen-java-service <serviceName>") }
  } describedAs "Generates Slim3 service in Java."

  lazy val genJavaGwtService = task { args =>
    if (args.length == 1)
      antTask {
        genJavaGwtServiceTask(args(0))
        genJavaGwtServiceImplTask(args(0))
      }
    else
      task { Some("Usage: gen-java-gwt-service <gwtServiceName>") }
  } describedAs "Generates Slim3 GWT service in Java."

  lazy val generateJavaMeta = generateJavaMetaAction
  def generateJavaMetaAction = antTask {
    generateJavaMetaTask()
  } describedAs "Generates Slim3 meta in Java."

  def genJavaControllerTask(controllerPath: String, useView: Boolean) {
    val task = new GenControllerTask
    task.setTaskName("gen-java-controller")
    task.setProject(antProject.value)
    task.setWarDir(webappPath.asFile)
    task.setSrcDir(mainJavaSourcePath.asFile)
    task.setTestDir(testJavaSourcePath.asFile)
    task.setControllerPath(controllerPath)
    task.setUseView(useView)
    task.setSuperclassName(s3ControllerSuperclass)
    task.execute()
  }

  def genJavaGwtServiceTask(serviceDefinition: String) {
    val task = new GenGWTServiceTask
    task.setTaskName("gen-java-gwt-service")
    task.setProject(antProject.value)
    task.setWarDir(webappPath.asFile)
    task.setSrcDir(mainJavaSourcePath.asFile)
    task.setTestDir(testJavaSourcePath.asFile)
    task.setServiceDefinition(serviceDefinition)
    task.setServiceClassNameProperty(Constants.SERVICE_CLASS_NAME_PROPERTY)
    task.execute()
  }

  def genJavaGwtServiceImplTask(serviceDefinition: String) {
    val task = new GenGWTServiceImplTask
    task.setTaskName("gen-java-gwt-service-impl")
    task.setProject(antProject.value)
    task.setWarDir(webappPath.asFile)
    task.setSrcDir(mainJavaSourcePath.asFile)
    task.setTestDir(testJavaSourcePath.asFile)
    task.setServiceDefinition(serviceDefinition)
    task.setServiceClassName(
      antProject.value.getProperty(Constants.SERVICE_CLASS_NAME_PROPERTY))
    task.execute()
  }

  def genJavaModelTask(modelDefinition: String) {
    val task = new GenModelTask
    task.setTaskName("gen-java-model")
    task.setProject(antProject.value)
    task.setWarDir(webappPath.asFile)
    task.setSrcDir(mainJavaSourcePath.asFile)
    task.setTestDir(testJavaSourcePath.asFile)
    task.setModelDefinition(modelDefinition)
    task.setModelClassNameProperty(Constants.MODEL_CLASS_NAME_PROPERTY)
    task.execute()
  }

  def genJavaServiceTask(serviceDefinition: String) {
    val task = new GenServiceTask
    task.setTaskName("gen-java-service")
    task.setProject(antProject.value)
    task.setWarDir(webappPath.asFile)
    task.setSrcDir(mainJavaSourcePath.asFile)
    task.setTestDir(testJavaSourcePath.asFile)
    task.setServiceDefinition(serviceDefinition)
    task.execute()
  }

  def genJavaViewTask(controllerPath: String) {
    val task = new GenViewTask
    task.setTaskName("gen-java-view")
    task.setProject(antProject.value)
    task.setWarDir(webappPath.asFile)
    task.setControllerPath(controllerPath)
    task.execute()
  }

  def generateJavaMetaTask() {
    if (mainSourceRoots.get.isEmpty) return

    generatedMainJavaSourcePath.asFile.mkdirs()

    val classpath = Path.makeString((compileClasspath +++ slim3GenJar).get)
    val srcdir    = Path.makeString(mainSourceRoots.get)

    val task = new Apt
    task.setTaskName("generate-java-meta")
    task.setProject(antProject.value)
    task.setClasspath(new AntPath(antProject.value, classpath))
    task.setFactory(Constants.MODEL_PROCESSOR_FACTORY)
    task.setSrcdir(new AntPath(antProject.value, srcdir))
    task.setPreprocessDir(generatedMainJavaSourcePath.asFile)
    task.setCompile(false)
    task.execute()
  }

  override def compileAction = super.compileAction dependsOn generateJavaMeta
}
