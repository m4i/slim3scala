package org.slim3scala.apt.mirror
package apt

import java.io.PrintWriter
import scala.io.Source

import com.sun.mirror.apt.Messager
import com.sun.mirror.util.SourcePosition

class MessagerImpl(env: AptEnv, out: PrintWriter)
    extends Messager {

  def printError(msg: String) {
    env.error = true
    out.println(buildMessage("ERROR", msg))
  }

  def printError(pos: SourcePosition, msg: String) {
    printError(buildMessage(pos, msg))
  }

  def printNotice(msg: String) {
    out.println(buildMessage("NOTICE", msg))
  }

  def printNotice(pos: SourcePosition, msg: String) {
    printNotice(buildMessage(pos, msg))
  }

  def printWarning(msg: String) {
    out.println(buildMessage("WARNING", msg))
  }

  def printWarning(pos: SourcePosition, msg: String) {
    printWarning(buildMessage(pos, msg))
  }

  protected def buildMessage(msg1: String, msg2: String): String = {
    msg1 + ": " + msg2
  }

  protected def buildMessage(pos: SourcePosition, msg: String): String = {
    if (pos == null) {
      msg
    } else {
      List(
        buildMessage(pos.file.getPath + ":" + pos.line, msg),
        Source.fromFile(pos.file).getLines.drop(pos.line - 1).next,
        " " * (pos.column - 1) + "^"
      ).mkString("\n")
    }
  }
}
