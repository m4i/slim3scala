package org.slim3scala.gen.generator

import org.slim3.gen.desc.{ ViewDesc => JViewDesc }
import org.slim3.gen.printer.Printer

import org.slim3scala.gen.Constants
import org.slim3scala.gen.desc.ViewDesc
import org.slim3scala.gen.util.SeqUtil

class ScalateViewGenerator(viewDesc: JViewDesc)
  extends ViewGenerator(viewDesc) {

  override def generate(p: Printer) {
    viewDesc.getFileName.split('.').last match {
      case "scaml" => generateScaml(p)
      case "ssp"   => generateSsp(p)
      case _       => throw new IllegalStateException(
        "The templateType must be %s.".format(
          SeqUtil.toSentence(Constants.SCALATE_TEMPLATE_TYPES, " or ")))
    }
  }

  def generateScaml(p: Printer) {
    p.println("-@ val controller: %s",
      viewDesc.asInstanceOf[ViewDesc].getControllerClassName)
    p.println()
    p.println("!!! 5")
    p.println("%%html")
    p.indent()
    p.println("%%head")
    p.indent()
    p.println("""%%meta(charset="UTF-8")""")
    p.println("%%title %s", viewDesc.getTitle)
    p.unindent()
    p.println("%%body")
    p.indent()
    p.println("%%p Hello %s !!!", viewDesc.getTitle)
  }

  def generateSsp(p: Printer) {
    p.println("<%%@ val controller: %s %%>",
      viewDesc.asInstanceOf[ViewDesc].getControllerClassName)

    p.println("<!DOCTYPE html>")
    p.println("<html>")
    p.println("<head>")
    p.println("""<meta charset="UTF-8" />""")
    p.println("<title>%s</title>", viewDesc.getTitle)
    p.println("</head>")
    p.println("<body>")
    p.println("<p>Hello %s !!!</p>", viewDesc.getTitle)
    p.println("</body>")
    p.println("</html>")
  }
}
