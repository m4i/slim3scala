package org.slim3scala.gen.task

import org.slim3.gen.desc.ViewDesc
import org.slim3.gen.desc.{ ViewDescFactory => JViewDescFactory }
import org.slim3.gen.generator.Generator

import org.slim3scala.gen.Constants
import org.slim3scala.gen.desc.ViewDescFactory
import org.slim3scala.gen.generator.ScalateViewGenerator
import org.slim3scala.gen.util.SeqUtil

class GenScalateViewTask extends GenViewTask {
  protected var scalateTemplateType: String =
    Constants.DEFAULT_SCALATE_TEMPLATE_TYPE

  def setScalateTemplateType(scalateTemplateType: String) {
    if (!Constants.SCALATE_TEMPLATE_TYPES.contains(scalateTemplateType)) {
      throw new IllegalArgumentException(
        "The scalateTemplateType must be %s.".format(
          SeqUtil.toSentence(Constants.SCALATE_TEMPLATE_TYPES, " or ")))
    }
    this.scalateTemplateType = scalateTemplateType
  }

  override protected def createViewDescFactory(): JViewDescFactory = {
    new ViewDescFactory(
      getControllerBasePackageName, "." + scalateTemplateType)
  }

  override protected def careateViewGenerator(
      viewDesc: ViewDesc): Generator = {
    new ScalateViewGenerator(viewDesc)
  }
}
