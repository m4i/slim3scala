package org.slim3scala.apt.mirror
package `type`

import com.sun.mirror.declaration.AnnotationTypeDeclaration
import com.sun.mirror.`type`.AnnotationType
import com.sun.mirror.util.TypeVisitor

trait AnnotationTypeImpl {
  val env: AptEnv

class Impl(_type: env.global.Type)
    extends env.InterfaceType.Impl(_type)
    with AnnotationType {

  override def accept(v: TypeVisitor) {
    v.visitAnnotationType(this)
  }

  override def getDeclaration: AnnotationTypeDeclaration = {
    env.declMaker.getTypeDeclaration(_type.typeSymbol)
      .asInstanceOf[AnnotationTypeDeclaration]
  }

  override def canEqual(other: Any): Boolean = {
    other.isInstanceOf[Impl]
  }

  override def equals(other: Any): Boolean = {
    other match {
      case that: Impl =>
        (that canEqual this) &&
        _type.typeSymbol.fullName == that._type.typeSymbol.fullName
      case _ => false
    }
  }
}

}
