package org.slim3scala.gen.generator

import org.slim3.gen.desc.ControllerDesc
import org.slim3.gen.generator.{ ControllerTestCaseGenerator => JControllerTestCaseGenerator }
import org.slim3.gen.printer.Printer

import org.slim3scala.gen.Constants

class ControllerTestCaseGenerator(controllerDesc: ControllerDesc)
  extends JControllerTestCaseGenerator(controllerDesc) {

  override def generate(p: Printer) {
    if (controllerDesc.getPackageName.nonEmpty) {
      p.println("package %s", controllerDesc.getPackageName)
      p.println()
    }

    p.println("import org.scalatest.FunSuite")
    p.println("import org.scalatest.matchers.ShouldMatchers")
    p.println()
    p.println("import org.slim3scala.tester.ControllerSuite")
    p.println()

    p.println("class %s%s",
      controllerDesc.getSimpleName, Constants.TEST_SUFFIX)
    p.indent()
    p.println("extends FunSuite with ControllerSuite with ShouldMatchers {")
    p.println()

    p.println("""test("run") {""")
    p.indent()
    p.println("""tester.start("%s")""", controllerDesc.getPath)
    p.println("val controller = tester.getController[%s]",
      controllerDesc.getSimpleName)
    p.println("controller should not equal (null)")
    p.println("tester.isRedirect should equal (false)")
    if (controllerDesc.isUseView) {
      p.println("""tester.getDestinationPath should equal ("%s")""",
        controllerDesc.getViewName)
    } else {
      p.println("tester.getDestinationPath should equal (null)")
    }
    p.unindent()
    p.println("}")

    p.unindent()
    p.println("}")
  }
}
