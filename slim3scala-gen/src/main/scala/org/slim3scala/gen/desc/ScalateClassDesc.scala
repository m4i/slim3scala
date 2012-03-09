package org.slim3scala.gen.desc

import java.io.File
import scala.reflect.BeanProperty

import org.slim3.gen.desc.ClassDesc

import org.fusesource.scalate.TemplateSource

class ScalateClassDesc extends ClassDesc {
  @BeanProperty
  protected var file: File = _

  @BeanProperty
  protected var uri: String = _

  lazy val templateSource = TemplateSource.fromFile(file, uri)

  def getPackageName: String   = templateSource.packageName
  def getSimpleName: String    = templateSource.simpleClassName
  def getQualifiedName: String = templateSource.className
}
