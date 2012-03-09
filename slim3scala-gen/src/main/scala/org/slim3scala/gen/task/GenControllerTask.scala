package org.slim3scala.gen.task

import org.slim3.gen.desc.ControllerDesc
import org.slim3.gen.desc.{ ControllerDescFactory => JControllerDescFactory }
import org.slim3.gen.generator.Generator
import org.slim3.gen.task.{ GenControllerTask => JGenControllerTask }

import org.slim3scala.gen.ClassConstants
import org.slim3scala.gen.Constants
import org.slim3scala.gen.desc.ControllerDescFactory
import org.slim3scala.gen.generator.ControllerGenerator
import org.slim3scala.gen.generator.ControllerTestCaseGenerator

class GenControllerTask
  extends JGenControllerTask with AbstractGenScalaFileTask {

  superclassName = ClassConstants.Controller

  protected var viewSuffix: String = Constants.VIEW_SUFFIX

  def setViewSuffix(viewSuffix: String) {
    this.viewSuffix = viewSuffix
  }

  override protected def createControllerDescFactory(
      controllerBasePackageName: String): JControllerDescFactory = {
    new ControllerDescFactory(
      controllerBasePackageName,
      superclassName,
      testCaseSuperclassName,
      useView,
      viewSuffix)
  }

  override protected def createControllerGenerator(
      controllerDesc: ControllerDesc): Generator = {
    new ControllerGenerator(controllerDesc)
  }

  override protected def createControllerTestCaseGenerator(
      controllerDesc: ControllerDesc): Generator = {
    new ControllerTestCaseGenerator(controllerDesc)
  }
}
