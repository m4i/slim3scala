package org.slim3scala.gen.util

import com.sun.mirror.declaration.FieldDeclaration

object FieldDeclarationUtil {
  def getReadMethodName(fieldDeclaration: FieldDeclaration): String = {
    if (fieldDeclaration == null) {
      throw new NullPointerException("The fieldDeclaration parameter is null.")
    }
    fieldDeclaration.getSimpleName
  }

  def getWriteMethodName(fieldDeclaration: FieldDeclaration): String = {
    if (fieldDeclaration == null) {
      throw new NullPointerException("The fieldDeclaration parameter is null.")
    }
    fieldDeclaration.getSimpleName + "_="
  }
}
