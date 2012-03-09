package org.slim3scala.gen.generator

import org.slim3.gen.desc.ServiceDesc
import org.slim3.gen.generator.{ ServiceTestCaseGenerator => JServiceTestCaseGenerator }
import org.slim3.gen.printer.Printer

import org.slim3scala.gen.Constants

class ServiceTestCaseGenerator(serviceDesc: ServiceDesc)
  extends JServiceTestCaseGenerator(serviceDesc) {

  override def generate(p: Printer) {
    if (serviceDesc.getPackageName.nonEmpty) {
      p.println("package %s", serviceDesc.getPackageName)
      p.println()
    }

    p.println("import org.scalatest.FunSuite")
    p.println("import org.scalatest.matchers.ShouldMatchers")
    p.println()
    p.println("import org.slim3scala.tester.AppEngineSuite")
    p.println()

    p.println("class %s%s", serviceDesc.getSimpleName, Constants.TEST_SUFFIX)
    p.indent()
    p.println("extends FunSuite with AppEngineSuite with ShouldMatchers {")
    p.println()

    p.println("private var service: %s = _", serviceDesc.getSimpleName)
    p.println()

    p.println("override def beforeEach() {")
    p.indent()
    p.println("super.beforeEach()")
    p.println("service = new %s", serviceDesc.getSimpleName)
    p.unindent()
    p.println("}")
    p.println()

    p.println("""test("test") {""")
    p.indent()
    p.println("service should not equal (null)")
    p.unindent()
    p.println("}")

    p.unindent()
    p.println("}")
  }
}
