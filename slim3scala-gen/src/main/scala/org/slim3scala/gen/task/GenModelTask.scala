package org.slim3scala.gen.task

import org.slim3.gen.desc.ModelDesc
import org.slim3.gen.generator.Generator
import org.slim3.gen.task.{ GenModelTask => JGenModelTask }

import org.slim3scala.gen.generator.ModelGenerator
import org.slim3scala.gen.generator.ModelTestCaseGenerator

class GenModelTask
  extends JGenModelTask with AbstractGenScalaFileTask {

  override protected def createModelGenerator(
      modelDesc: ModelDesc): Generator = {
    new ModelGenerator(modelDesc)
  }

  override protected def createModelTestCaseGenerator(
      modelDesc: ModelDesc): Generator = {
    new ModelTestCaseGenerator(modelDesc)
  }
}
