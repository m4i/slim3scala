package org.slim3scala.gen.task

import org.slim3.gen.desc.ServiceDesc
import org.slim3.gen.generator.Generator
import org.slim3.gen.task.{ GenServiceTask => JGenServiceTask }

import org.slim3scala.gen.generator.ServiceGenerator
import org.slim3scala.gen.generator.ServiceTestCaseGenerator

class GenServiceTask
  extends JGenServiceTask with AbstractGenScalaFileTask {

  override protected def createServiceGenerator(
      serviceDesc: ServiceDesc): Generator = {
    new ServiceGenerator(serviceDesc)
  }

  override protected def createServiceTestCaseGenerator(
      serviceDesc: ServiceDesc): Generator = {
    new ServiceTestCaseGenerator(serviceDesc)
  }
}
