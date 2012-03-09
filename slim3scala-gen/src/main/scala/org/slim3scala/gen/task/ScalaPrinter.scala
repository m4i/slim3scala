package org.slim3scala.gen.task

import java.io.File

import org.slim3.gen.printer.Printer
import org.slim3.gen.task.AbstractGenFileTask

import org.slim3scala.gen.printer.FilePrinter

trait ScalaPrinter extends AbstractGenFileTask {
  private var _encoding = "UTF-8"

  /**
   * trait cannot access java protected variable encoding
   */
  override def setEncoding(encoding: String) {
    super.setEncoding(encoding)
    _encoding = encoding
  }

  override protected def createPrinter(file: File): Printer = {
    new FilePrinter(file, _encoding)
  }
}
