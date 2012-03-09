package org.slim3scala.gen.generator

import org.slim3.gen.AnnotationConstants
import org.slim3.gen.ClassConstants
import org.slim3.gen.desc.ModelDesc
import org.slim3.gen.generator.{ ModelGenerator => JModelGenerator }
import org.slim3.gen.printer.Printer
import org.slim3.gen.util.ClassUtil

class ModelGenerator(modelDesc: ModelDesc)
  extends JModelGenerator(modelDesc) {

  override def generate(p: Printer) {
    printPackage(p)
    printImport(p)
    printClassHeader(p)
    if (ClassConstants.Object == modelDesc.getSuperclassName) {
      printAttributes(p)
      printHashCodeMethod(p)
      printEqualMethod(p)
      printCanEqualMethod(p)
    }
    printClassFooter(p)
  }

  protected def printPackage(p: Printer) {
    if (modelDesc.getPackageName.nonEmpty) {
      p.println("package %s", modelDesc.getPackageName)
      p.println()
    }
  }

  protected def printImport(p: Printer) {
    if (ClassConstants.Object == modelDesc.getSuperclassName) {
      p.println("import com.google.appengine.api.datastore.Key")
      p.println()
      p.println("import org.slim3.datastore.Attribute")
      p.println("import org.slim3.datastore.Model")
    } else {
      p.println("import org.slim3.datastore.Model")
      if (ClassUtil.getPackageName(modelDesc.getSuperclassName) !=
          modelDesc.getPackageName) {
        p.println()
        p.println("import %s", modelDesc.getSuperclassName)
      }
    }
    p.println()
  }

  protected def printClassHeader(p: Printer) {
    p.println("@SerialVersionUID(1L)")
    p.println("@Model(%1$s = 1)", AnnotationConstants.schemaVersion)
    if (ClassConstants.Object == modelDesc.getSuperclassName) {
      p.println("class %s {", modelDesc.getSimpleName)
    } else {
      p.println("class %s extends %s {",
        modelDesc.getSimpleName,
        ClassUtil.getSimpleName(modelDesc.getSuperclassName))
    }
    p.indent()
  }

  protected def printAttributes(p: Printer) {
    p.println("@Attribute(primaryKey = true)")
    p.println("var key: Key = _")
    p.println()
    p.println("@Attribute(version = true)")
    p.println("var version: Long = _")
  }

  protected def printHashCodeMethod(p: Printer) {
    p.println()
    p.println("override def hashCode: Int = {")
    p.indent()
    p.println("41 + (if (key == null) 0 else key.hashCode)")
    p.unindent()
    p.println("}")
  }

  protected def printEqualMethod(p: Printer) {
    p.println()
    p.println("override def equals(other: Any): Boolean = other match {")
    p.indent()
    p.println("case that: %s =>", modelDesc.getSimpleName)
    p.indent()
    p.println("(that canEqual this) &&")
    p.println("key == that.key")
    p.unindent()
    p.println("case _ => false")
    p.unindent()
    p.println("}")
  }

  protected def printCanEqualMethod(p: Printer) {
    p.println()
    p.println("def canEqual(other: Any): Boolean = {")
    p.indent()
    p.println("other.isInstanceOf[%s]", modelDesc.getSimpleName)
    p.unindent()
    p.println("}")
  }

  protected def printClassFooter(p: Printer) {
    p.unindent()
    p.println("}")
  }
}
