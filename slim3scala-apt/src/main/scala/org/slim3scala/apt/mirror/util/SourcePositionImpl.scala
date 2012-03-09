package org.slim3scala.apt.mirror
package util

import java.io.File

import com.sun.mirror.util.SourcePosition

import scala.tools.nsc.util.Position

class SourcePositionImpl(position: Position)
    extends SourcePosition {

  val file   = new File(position.source.path)
  val line   = position.line
  val column = position.column
}
