package org.slim3scala.gen.desc

import org.slim3.gen.{ Constants => JConstants }
import org.slim3.gen.desc.{ ViewDesc => JViewDesc }
import org.slim3.gen.desc.{ ViewDescFactory => JViewDescFactory }
import org.slim3.gen.util.StringUtil

import org.slim3scala.gen.Constants

class ViewDescFactory(controllerPackageName: String, viewSuffix: String)
  extends JViewDescFactory {

  if (controllerPackageName == null) {
      throw new NullPointerException(
        "The controllerPackageName parameter is null.")
  }
  if (viewSuffix == null) {
      throw new NullPointerException(
        "The viewSuffix parameter is null.")
  }

  override def createViewDesc(path: String): JViewDesc = {
    val jViewDesc = super.createViewDesc(path)
    val fileName = jViewDesc.getFileName.dropRight(
      JConstants.VIEW_SUFFIX.length) + Constants.VIEW_SUFFIX + viewSuffix
    val viewDesc = new ViewDesc
    viewDesc.setDirName(jViewDesc.getDirName)
    viewDesc.setFileName(fileName)
    viewDesc.setRelativePath(jViewDesc.getRelativePath)
    viewDesc.setTitle(jViewDesc.getTitle)
    viewDesc.setControllerClassName(toControllerClassName(path))
    viewDesc
  }

  protected def toControllerClassName(path: String): String = {
    var className = controllerPackageName + path.replace('/', '.')
    if (className.endsWith(".")) {
      className += Constants.INDEX_CONTROLLER
    } else {
      val pos = className.lastIndexOf('.')
      className = className.substring(0, pos + 1) +
        StringUtil.capitalize(className.substring(pos + 1)) +
        Constants.CONTROLLER_SUFFIX
    }
    className
  }
}
