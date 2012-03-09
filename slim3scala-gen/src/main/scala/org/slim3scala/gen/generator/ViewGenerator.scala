package org.slim3scala.gen.generator

import org.slim3.gen.desc.{ ViewDesc => JViewDesc }
import org.slim3.gen.generator.{ ViewGenerator => JViewGenerator }
import org.slim3.gen.printer.Printer

import org.slim3scala.gen.desc.ViewDesc

class ViewGenerator(viewDesc: JViewDesc) extends JViewGenerator(viewDesc) {
  if (!viewDesc.isInstanceOf[ViewDesc]) {
    throw new IllegalArgumentException(
      "The viewDesc must be %s.".format(classOf[ViewDesc].getName))
  }

  override def generate(p: Printer) {
    p.println("val controller = this.controller.asInstanceOf[%s]",
      viewDesc.asInstanceOf[ViewDesc].getControllerClassName)
    p.println()
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
