package org.slim3scala.apt.mirror
package `type`

import com.sun.mirror.`type`.PrimitiveType
import com.sun.mirror.util.TypeVisitor

trait PrimitiveTypeImpl {
  val env: AptEnv

class Impl(kind: PrimitiveType.Kind)
    extends env.TypeMirror.Impl
    with PrimitiveType {

  def accept(v: TypeVisitor) {
    v.visitPrimitiveType(this)
  }

  def getKind: PrimitiveType.Kind = {
    kind
  }

  def canEqual(other: Any): Boolean = {
    other.isInstanceOf[Impl]
  }

  override def equals(other: Any): Boolean = {
    other match {
      case that: Impl =>
        (that canEqual this) &&
        kind == that.getKind
      case _ => false
    }
  }

  override val hashCode: Int = {
    41 + kind.hashCode
  }
}

}
