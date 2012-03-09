package org.slim3scala.gen.task

import java.io.File

import org.slim3.gen.desc.ClassDesc
import org.slim3.gen.task.JavaFile

class ScalaFile(baseDir: File, classDesc: ClassDesc, suffix: String)
  extends JavaFile(baseDir, classDesc, suffix) {

  def this(baseDir: File, classDesc: ClassDesc) =
    this(baseDir, classDesc, "")

  override protected def createFile(
      baseDir: File, classDesc: ClassDesc, suffix: String): File = {
    val file = super.createFile(baseDir, classDesc, suffix)
    new File(file.getParent, file.getName.dropRight(4) + "scala")
  }
}
