package org.slim3scala.gen.task

import java.io.File
import java.io.FileFilter

import org.slim3.gen.desc.ClassDesc
import org.slim3.gen.generator.Generator

import org.slim3scala.gen.desc.ScalateClassDesc
import org.slim3scala.gen.generator.ScalateClassGenerator

import org.fusesource.scalate.Binding
import org.fusesource.scalate.TemplateEngine
import org.fusesource.scalate.servlet.ServletRenderContext

class GenerateScalateClassTask extends AbstractGenerateTemplateClassTask {
  protected lazy val scalateEngine: TemplateEngine = {
    val engine = new TemplateEngine
    engine.bindings = Binding("context",
      classOf[ServletRenderContext].getName, true, isImplicit = true) :: Nil
    engine
  }

  protected def createClassDesc(srcFile: File): ClassDesc = {
    if (!classDescCache.contains(srcFile)) {
      val uri = viewBasePackageName.replace('.', '/') +
        srcFile.getAbsolutePath.drop(viewDir.getAbsolutePath.length)

      val classDesc = new ScalateClassDesc
      classDesc.setFile(srcFile)
      classDesc.setUri(uri)

      classDescCache(srcFile) = classDesc
    }
    classDescCache(srcFile)
  }

  protected def createGenerator(classDesc: ClassDesc): Generator = {
    new ScalateClassGenerator(
      classDesc.asInstanceOf[ScalateClassDesc], scalateEngine)
  }

  protected val srcFilter = new FileFilter {
    def accept(f: File) =
      f.isDirectory ||
      !f.getName.startsWith(".") &&
      isScalateTemplateFile(f)
  }

  protected val dstFilter = new FileFilter {
    def accept(f: File) =
      f.isDirectory ||
      !f.getName.startsWith(".") &&
      f.getName.endsWith(".scala") &&
      isScalateClassFile(f)
  }
}
