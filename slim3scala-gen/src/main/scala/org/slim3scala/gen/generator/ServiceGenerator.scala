package org.slim3scala.gen.generator

import org.slim3.gen.ClassConstants
import org.slim3.gen.desc.ServiceDesc
import org.slim3.gen.generator.{ ServiceGenerator => JServiceGenerator }
import org.slim3.gen.printer.Printer

class ServiceGenerator(serviceDesc: ServiceDesc)
  extends JServiceGenerator(serviceDesc) {

  override def generate(p: Printer) {
    if (serviceDesc.getPackageName.nonEmpty) {
      p.println("package %s", serviceDesc.getPackageName)
      p.println()
    }

    if (ClassConstants.Object != serviceDesc.getSuperclassName) {
      p.println("import %s", serviceDesc.getSuperclassName)
      p.println()
    }

    p.print("class %s", serviceDesc.getSimpleName)
    if (ClassConstants.Object != serviceDesc.getSuperclassName) {
      p.print(" extends %s", serviceDesc.getSuperclassName)
    }
    p.println(" {")
    p.println("}")
  }
}
