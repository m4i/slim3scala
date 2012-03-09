package org.slim3scala.gen.generator

import org.slim3.gen.generator.Generator
import org.slim3.gen.printer.Printer

import org.slim3scala.gen.desc.ScalateClassDesc
import org.slim3scala.gen.printer.FilePrinter

import org.fusesource.scalate.TemplateEngine

class ScalateClassGenerator(
    scalateClassDesc: ScalateClassDesc, engine: TemplateEngine)
  extends Generator {

  def generate(p: Printer) {
    if (!p.isInstanceOf[FilePrinter]) {
      throw new IllegalArgumentException(
        "The p must be %s.".format(classOf[FilePrinter].getName))
    }

    val code = engine.generateScala(scalateClassDesc.templateSource)
    p.asInstanceOf[FilePrinter].write(code.source)
  }
}
