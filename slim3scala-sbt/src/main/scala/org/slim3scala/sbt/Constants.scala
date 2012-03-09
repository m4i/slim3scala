package org.slim3scala.sbt

import org.slim3.gen.{ Constants => JConstants }

object Constants {
  val VIEW_SUFFIX   = ".html"
  val META_PACKAGE  = JConstants.META_PACKAGE
  val MODEL_PACKAGE = JConstants.MODEL_PACKAGE

  val MODEL_CLASS_NAME_PROPERTY   = "modelClassName"
  val MODEL_PROCESSOR_FACTORY     = "org.slim3.gen.processor.ModelProcessorFactory"
  val ROOT_PACKAGE_KEY            = "slim3.rootPackage"
  val SERVICE_CLASS_NAME_PROPERTY = "serviceClassName"
  val VIEW_PACKAGE                = "view"
}
