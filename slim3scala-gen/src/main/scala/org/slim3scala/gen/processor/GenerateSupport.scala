package org.slim3scala.gen.processor

import java.io.Writer

import com.sun.mirror.apt.AnnotationProcessorEnvironment

import org.slim3.gen.printer.Printer

import org.slim3scala.gen.printer.FilePrinter

class GenerateSupport(env: AnnotationProcessorEnvironment)
    extends org.slim3.gen.processor.GenerateSupport(env) {

  override protected def createPrinter(writer: Writer): Printer = {
    new FilePrinter(writer)
  }
}
