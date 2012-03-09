package org.slim3scala.gen.task

import java.io.File
import java.io.FileFilter

import org.slim3.gen.desc.ClassDesc
import org.slim3.gen.generator.Generator

import org.slim3scala.gen.Constants
import org.slim3scala.gen.ClassConstants
import org.slim3scala.gen.desc.TemplateClassDesc
import org.slim3scala.gen.generator.TemplateClassGenerator

class GenerateTemplateClassTask extends AbstractGenerateTemplateClassTask {
  protected var superclassName: String = ClassConstants.Template
  protected var viewClassName: String  = ClassConstants.View

  def setSuperclassName(superclassName: String) {
    this.superclassName = superclassName
  }

  def setViewClassName(viewClassName: String) {
    this.viewClassName = viewClassName
  }

  protected def createClassDesc(srcFile: File): ClassDesc = {
    if (!classDescCache.contains(srcFile)) {
      val path                   = toTemplatePath(srcFile)
      val templateClassNameParts = toTemplateClassName(path).split('.')

      val classDesc = new TemplateClassDesc
      classDesc.setPackageName(templateClassNameParts.init.mkString("."))
      classDesc.setSimpleName(templateClassNameParts.last)
      classDesc.setSuperclassName(superclassName)
      classDesc.setSrcFile(srcFile)
      classDesc.setPath(path)
      classDesc.setViewClassName(viewClassName)

      classDescCache(srcFile) = classDesc
    }
    classDescCache(srcFile)
  }

  protected def createGenerator(classDesc: ClassDesc): Generator = {
    new TemplateClassGenerator(classDesc.asInstanceOf[TemplateClassDesc])
  }

  protected def toTemplatePath(srcFile: File): String = {
    srcFile.getAbsolutePath.drop(viewDir.getAbsolutePath.length)
  }

  protected def toTemplateClassName(path: String): String = {
    val parts = path.split('/')
    viewBasePackageName +
      parts.init.mkString(".") +
      "." +
      parts.last.split('.').map(_.capitalize).mkString +
      Constants.TEMPLATE_SUFFIX
  }

  protected val srcFilter = new FileFilter {
    def accept(f: File) =
      f.isDirectory ||
      !f.getName.startsWith(".") &&
      !isScalateTemplateFile(f)
  }

  protected val dstFilter = new FileFilter {
    def accept(f: File) =
      f.isDirectory ||
      !f.getName.startsWith(".") &&
      f.getName.endsWith(".scala") &&
      !isScalateClassFile(f)
  }
}
