package org.slim3scala.apt.mirror
package `type`

import com.sun.mirror.declaration.InterfaceDeclaration
import com.sun.mirror.`type`.InterfaceType
import com.sun.mirror.util.TypeVisitor

trait InterfaceTypeImpl {
  val env: AptEnv

class Impl(_type: env.global.Type)
    extends env.DeclaredType.Impl(_type)
    with InterfaceType {

  def accept(v: TypeVisitor) {
    v.visitInterfaceType(this)
  }

  def getDeclaration: InterfaceDeclaration = {
    env.declMaker.getTypeDeclaration(_type.typeSymbol)
      .asInstanceOf[InterfaceDeclaration]
  }

  def canEqual(other: Any): Boolean = {
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
