package org.slim3scala.gen.task

import java.io.File
import java.io.FileFilter
import scala.collection.mutable

import org.slim3.gen.desc.ClassDesc
import org.slim3.gen.generator.Generator
import org.slim3.gen.task.JavaFile

import org.slim3scala.gen.Constants

import org.fusesource.scalate.TemplateEngine

abstract class AbstractGenerateTemplateClassTask
  extends AbstractGenScalaFileTask {

  testDir = new File("dummy")

  protected var viewDir: File = _

  def setViewDir(viewDir: File) {
    this.viewDir = viewDir
  }

  override def doExecute() {
    super.doExecute()
    if (viewDir == null) {
      throw new IllegalStateException("The viewDir parameter is null.")
    }
    generate()
    clean()
  }

  protected def generate() {
    for (srcFile <- srcFiles) {
      val classDesc = createClassDesc(srcFile)
      val javaFile  = createJavaFile(classDesc)
      val generator = createGenerator(classDesc)
      generateJavaFile(generator, javaFile, srcFile)
    }
  }

  protected def clean() {
    for (dstFile <- (existsDstFiles -- expectedDstFiles)) {
      dstFile.delete()
    }
  }

  protected val javaFileCache  = mutable.Map[ClassDesc, JavaFile]()
  protected val classDescCache = mutable.Map[File, ClassDesc]()

  override protected def createJavaFile(classDesc: ClassDesc): JavaFile = {
    if (!javaFileCache.contains(classDesc)) {
      javaFileCache(classDesc) = super.createJavaFile(classDesc)
    }
    javaFileCache(classDesc)
  }

  protected def generateJavaFile(
      generator: Generator, javaFile: JavaFile, srcFile: File) {
    val file = javaFile.getFile
    if (!(file.exists && file.lastModified >= srcFile.lastModified)) {
      generateJavaFileWithoutCheck(generator, javaFile)
    }
  }

  protected def createClassDesc(srcFile: File): ClassDesc

  protected def createGenerator(classDesc: ClassDesc): Generator

  protected val srcFilter: FileFilter

  protected val dstFilter: FileFilter

  protected lazy val rootPackageName = createWebConfig.getRootPackageName

  protected lazy val viewBasePackageName =
    rootPackageName + "." + Constants.VIEW_PACKAGE

  protected lazy val dstViewDir =
    new File(srcDir, viewBasePackageName.replace('.', '/'))

  protected lazy val srcFiles = findFiles(viewDir, srcFilter)

  protected def existsDstFiles = findFiles(dstViewDir, dstFilter)

  protected lazy val expectedDstFiles =
    for (srcFile <- srcFiles)
      yield createJavaFile(createClassDesc(srcFile)).getFile

  protected def isScalateTemplateFile(file: File): Boolean = {
    TemplateEngine.templateTypes.exists(e => file.getName.endsWith("." + e))
  }

  protected def isScalateClassFile(file: File): Boolean = {
    file.getName.startsWith(Constants.SCALATE_TEMPLATE_PREFIX)
  }

  protected def findFiles(dir: File, filter: FileFilter): Set[File] = {
    (Set[File]() /: {
      val files = dir.listFiles(filter)
      if (files == null)
        Array[Set[File]]()
      else
        for (file <- files)
          yield if (file.isDirectory) findFiles(file, filter) else Set(file)
    })(_ | _)
  }
}
