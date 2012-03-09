package org.slim3scala.apt.mirror
package `type`

import com.sun.mirror.declaration.ClassDeclaration
import com.sun.mirror.`type`.ClassType
import com.sun.mirror.util.TypeVisitor

trait ClassTypeImpl {
  val env: AptEnv

class Impl(_type: env.global.Type)
    extends env.DeclaredType.Impl(_type)
    with ClassType {

  def accept(v: TypeVisitor) {
    v.visitClassType(this)
  }

  def getDeclaration: ClassDeclaration = {
    env.declMaker.getTypeDeclaration(_type.typeSymbol)
      .asInstanceOf[ClassDeclaration]
  }

  def getSuperclass: ClassType = {
    _type.typeSymbol match {
      case env.global.definitions.AnyClass =>
        null
      case s =>
        env.typeMaker.getType(s.superClass.info).asInstanceOf[ClassType]
    }
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
