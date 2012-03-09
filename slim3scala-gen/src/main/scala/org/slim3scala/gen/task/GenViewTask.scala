package org.slim3scala.gen.task

import java.io.File

import org.slim3.gen.desc.ViewDesc
import org.slim3.gen.desc.{ ViewDescFactory => JViewDescFactory }
import org.slim3.gen.generator.Generator
import org.slim3.gen.task.{ GenViewTask => JGenViewTask }

import org.slim3scala.gen.Constants
import org.slim3scala.gen.desc.ViewDescFactory
import org.slim3scala.gen.generator.ViewGenerator

class GenViewTask extends JGenViewTask with ScalaPrinter {
  protected var viewDir: File = _

  def setViewDir(viewDir: File) {
    this.viewDir = viewDir
  }

  override protected def createViewDescFactory(): JViewDescFactory = {
    new ViewDescFactory(getControllerBasePackageName, "")
  }

  override protected def generateView(viewDesc: ViewDesc) {
    if (this.viewDir == null) {
      throw new IllegalStateException("The viewDir parameter is null.")
    }
    val viewDir = new File(this.viewDir, viewDesc.getDirName)
    viewDir.mkdirs()
    val viewFile = new File(viewDir, viewDesc.getFileName)
    val generator = careateViewGenerator(viewDesc)
    generateFile(generator, viewFile)
  }

  override protected def careateViewGenerator(
      viewDesc: ViewDesc): Generator = {
    new ViewGenerator(viewDesc)
  }

  protected def getControllerBasePackageName: String = {
    val buf = new StringBuilder
    val config = createWebConfig()
    buf.append(config.getRootPackageName)
    if (config.isGWTServiceServletDefined) {
      buf.append(".")
      buf.append(Constants.SERVER_PACKAGE)
    }
    buf.append(".")
    buf.append(Constants.CONTROLLER_PACKAGE)
    buf.toString
  }
}
