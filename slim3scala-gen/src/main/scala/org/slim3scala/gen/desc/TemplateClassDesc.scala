package org.slim3scala.gen.desc

import java.io.File
import scala.io.Source
import scala.reflect.BeanProperty

import org.slim3.gen.desc.ClassDesc
import org.slim3.gen.util.ClassUtil

class TemplateClassDesc extends ClassDesc {
  @BeanProperty
  protected var packageName: String = _

  @BeanProperty
  protected var simpleName: String = _

  @BeanProperty
  protected var superclassName: String = _

  @BeanProperty
  protected var srcFile: File = _

  @BeanProperty
  protected var path: String = _

  @BeanProperty
  protected var viewClassName: String = _

  lazy val getTemplate: String = Source.fromFile(srcFile).mkString

  def getQualifiedName: String = {
    ClassUtil.getQualifiedName(packageName, simpleName)
  }
}
