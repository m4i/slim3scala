package org.slim3scala.apt.mirror
package `type`

import com.sun.mirror.`type`.VoidType
import com.sun.mirror.util.TypeVisitor

trait VoidTypeImpl {
  val env: AptEnv

class Impl
    extends env.TypeMirror.Impl
    with VoidType {

  def accept(v: TypeVisitor) {
    v.visitVoidType(this)
  }

  def canEqual(other: Any): Boolean = {
    other.isInstanceOf[Impl]
  }

  override def equals(other: Any): Boolean = {
    other match {
      case that: Impl => (that canEqual this)
      case _          => false
    }
  }

  override val hashCode: Int = {
    41
  }
}

}
