package org.slim3scala.gen.generator

import org.slim3.gen.generator.Generator
import org.slim3.gen.printer.Printer

import org.slim3scala.gen.desc.TemplateClassDesc
import org.slim3scala.gen.printer.FilePrinter

class TemplateClassGenerator(templateClassDesc: TemplateClassDesc)
  extends Generator {

  def generate(p: Printer) {
    if (!p.isInstanceOf[FilePrinter]) {
      throw new IllegalArgumentException(
        "The p must be %s.".format(classOf[FilePrinter].getName))
    }

    if (templateClassDesc.getPackageName.nonEmpty) {
      p.print("package %s; ", templateClassDesc.getPackageName)
    }

    p.print("class %s(view: _root_.%s) extends _root_.%s(view) { ",
      templateClassDesc.getSimpleName,
      templateClassDesc.getViewClassName,
      templateClassDesc.getSuperclassName)

    p.print("def render(): Any = { ")
    p.asInstanceOf[FilePrinter].write(templateClassDesc.getTemplate)

    p.indent()
    p.println("}")
    p.println()

    p.println("protected val path: String = \"%s\"", templateClassDesc.getPath)

    p.unindent()
    p.println("}")
  }
}
