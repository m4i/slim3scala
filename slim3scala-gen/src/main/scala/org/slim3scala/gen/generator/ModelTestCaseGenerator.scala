package org.slim3scala.gen.generator

import org.slim3.gen.desc.ModelDesc
import org.slim3.gen.generator.{ ModelTestCaseGenerator => JModelTestCaseGenerator }
import org.slim3.gen.printer.Printer

import org.slim3scala.gen.Constants

class ModelTestCaseGenerator(modelDesc: ModelDesc)
  extends JModelTestCaseGenerator(modelDesc) {

  override def generate(p: Printer) {
    if (modelDesc.getPackageName.nonEmpty) {
      p.println("package %s", modelDesc.getPackageName)
      p.println()
    }

    p.println("import org.scalatest.FunSuite")
    p.println("import org.scalatest.matchers.ShouldMatchers")
    p.println()
    p.println("import org.slim3scala.tester.AppEngineSuite")
    p.println()

    p.println("class %s%s", modelDesc.getSimpleName, Constants.TEST_SUFFIX)
    p.indent()
    p.println("extends FunSuite with AppEngineSuite with ShouldMatchers {")
    p.println()

    p.println("private var model: %s = _", modelDesc.getSimpleName)
    p.println()

    p.println("override def beforeEach() {")
    p.indent()
    p.println("super.beforeEach()")
    p.println("model = new %s", modelDesc.getSimpleName)
    p.unindent()
    p.println("}")
    p.println()

    p.println("""test("test") {""")
    p.indent()
    p.println("model should not equal (null)")
    p.unindent()
    p.println("}")

    p.unindent()
    p.println("}")
  }
}
