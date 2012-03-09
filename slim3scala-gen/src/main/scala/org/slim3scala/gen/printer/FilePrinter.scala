package org.slim3scala.gen.printer

import java.lang.{ StringBuilder => JStringBuilder }
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.io.Writer

import org.slim3.gen.printer.{ FilePrinter => JFilePrinter }

class FilePrinter(writer: Writer) extends JFilePrinter(writer) {
  def this(file: File, encoding: String) =
    this(new OutputStreamWriter(new FileOutputStream(file), encoding))

  protected val SCALA_INDENT_SPACE = "  "

  protected val _indent = getClass.getSuperclass
    .getDeclaredField("indent").get(this).asInstanceOf[JStringBuilder]

  override def indent() {
    _indent.append(SCALA_INDENT_SPACE)
  }

  override def unindent() {
    if (_indent.length >= SCALA_INDENT_SPACE.length) {
      _indent.setLength(_indent.length - SCALA_INDENT_SPACE.length)
    }
  }

  def write(str: String) {
    formatter.out.append(str)
  }
}
