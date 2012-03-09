package org.slim3scala.apt.mirror
package apt

import java.io.{ File, OutputStream, PrintWriter }

import com.sun.mirror.apt.Filer
import com.sun.mirror.apt.Filer.Location

class FilerImpl(baseDir: String)
    extends Filer {

  val sourceExtension = ".scala"

  def createSourceFile(name: String): PrintWriter = {
    val file = new File(baseDir, name.replace(".", "/") + sourceExtension)
    file.getParentFile.mkdirs()
    new PrintWriter(file)
  }

  def createBinaryFile(loc: Location, pkg: String, relPath: File): OutputStream = throw new NotImplementedException
  def createClassFile(name: String): OutputStream = throw new NotImplementedException
  def createTextFile(loc: Location, pkg: String, relPath: File, charsetName: String): PrintWriter = throw new NotImplementedException
}
