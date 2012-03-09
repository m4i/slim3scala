package org.slim3scala.gen

import org.slim3.gen.{ Constants => JConstants }

object Constants {
  val TEST_SUFFIX        = "Suite"
  val CONTROLLER_SUFFIX  = JConstants.CONTROLLER_SUFFIX
  val VIEW_SUFFIX        = ".html"
  val INDEX              = JConstants.INDEX
  val INDEX_CONTROLLER   = JConstants.INDEX_CONTROLLER
  val INDEX_VIEW         = JConstants.INDEX + VIEW_SUFFIX
  val SERVER_PACKAGE     = JConstants.SERVER_PACKAGE
  val CONTROLLER_PACKAGE = JConstants.CONTROLLER_PACKAGE

  val DEFAULT_SCALATE_TEMPLATE_TYPE = "scaml"
  val SCALATE_TEMPLATE_PREFIX       = "$_scalate_$"
  val SCALATE_TEMPLATE_TYPES        = List("scaml", "ssp")
  val TEMPLATE_SUFFIX               = "Template"
  val VIEW_PACKAGE                  = "view"
}
