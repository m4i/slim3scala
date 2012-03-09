package org.slim3scala.gen.task

import java.io.File

import org.slim3.gen.desc.ClassDesc
import org.slim3.gen.generator.Generator
import org.slim3.gen.message.MessageCode
import org.slim3.gen.message.MessageFormatter
import org.slim3.gen.printer.Printer
import org.slim3.gen.task.AbstractGenJavaFileTask
import org.slim3.gen.task.JavaFile

import org.slim3scala.gen.Constants

trait AbstractGenScalaFileTask
  extends AbstractGenJavaFileTask with ScalaPrinter {

  private var _srcDir: File = _
  private var _testDir: File = _

  /**
   * trait cannot access java protected variable srcDir
   */
  override def setSrcDir(srcDir: File) {
    super.setSrcDir(srcDir)
    _srcDir = srcDir
  }

  /**
   * trait cannot access java protected variable testDir
   */
  override def setTestDir(testDir: File) {
    super.setTestDir(testDir)
    _testDir = testDir
  }

  override protected def createJavaFile(classDesc: ClassDesc): JavaFile = {
    new ScalaFile(_srcDir, classDesc)
  }

  override protected def createTestCaseJavaFile(
      classDesc: ClassDesc): JavaFile = {
    new ScalaFile(_testDir, classDesc, Constants.TEST_SUFFIX)
  }

  override protected def generateJavaFile(
      generator: Generator, javaFile: JavaFile) {
    if (javaFile.getFile.exists) {
      log(modifyMessageExtension(
        MessageFormatter.getSimpleMessage(
          MessageCode.SLIM3GEN0004,
          javaFile.getClassName)))
    } else {
      generateJavaFileWithoutCheck(generator, javaFile)
    }
  }

  protected def generateJavaFileWithoutCheck(
      generator: Generator, javaFile: JavaFile) {
    var printer: Printer = null
    try {
      printer = createPrinter(javaFile.getFile)
      generator.generate(printer)
    } finally {
      if (printer != null) {
        printer.close()
      }
    }
    log(modifyMessageExtension(
      MessageFormatter.getSimpleMessage(
        MessageCode.SLIM3GEN0005,
        javaFile.getClassName)))
  }

  protected def modifyMessageExtension(message: String): String = {
    message.replaceFirst("""\.java((?:(?!\.java).)*)$""", ".scala$1")
  }
}
