package org.slim3scala.gen.generator

import org.slim3.gen.ClassConstants
import org.slim3.gen.desc.ControllerDesc
import org.slim3.gen.generator.{ ControllerGenerator => JControllerGenerator }
import org.slim3.gen.printer.Printer
import org.slim3.gen.util.ClassUtil

class ControllerGenerator(controllerDesc: ControllerDesc)
  extends JControllerGenerator(controllerDesc) {

  override def generate(p: Printer) {
    printPackage(p)
    printImport(p)
    printClassHeader(p)
    printRunMethod(p)
    printClassFooter(p)
  }

  protected def printPackage(p: Printer) {
    if (controllerDesc.getPackageName.nonEmpty) {
      p.println("package %s", controllerDesc.getPackageName)
      p.println()
    }
  }

  protected def printImport(p: Printer) {
    p.println("import %s", ClassConstants.Navigation)
    if (ClassUtil.getPackageName(controllerDesc.getSuperclassName) !=
        controllerDesc.getPackageName) {
      p.println("import %s", controllerDesc.getSuperclassName)
    }
    p.println()
  }

  protected def printClassHeader(p: Printer) {
    p.println("class %s extends %s {",
      controllerDesc.getSimpleName,
      ClassUtil.getSimpleName(controllerDesc.getSuperclassName))
    p.indent()
  }

  protected def printRunMethod(p: Printer) {
    p.println("protected def run(): %s = {",
      ClassUtil.getSimpleName(ClassConstants.Navigation))
    p.indent()
    if (controllerDesc.isUseView) {
      p.println("""forward("%s")""", controllerDesc.getSimpleViewName)
    } else {
      p.println("null")
    }
    p.unindent()
    p.println("}")
  }

  protected def printClassFooter(p: Printer) {
    p.unindent()
    p.println("}")
  }
}
