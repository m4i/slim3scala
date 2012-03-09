package org.slim3scala.gen.desc

import org.slim3.gen.desc.{ ControllerDescFactory => JControllerDescFactory }

import org.slim3scala.gen.Constants

class ControllerDescFactory(
    packageName: String,
    superclassName: String,
    testCaseSuperclassName: String,
    useView: Boolean,
    viewSuffix: String)
  extends JControllerDescFactory(
    packageName, superclassName, testCaseSuperclassName, useView) {

  override protected def toViewName(path: String): String = {
    if (path.endsWith("/")) path + Constants.INDEX + viewSuffix
    else                    path + viewSuffix
  }
}
