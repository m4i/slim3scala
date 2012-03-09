package org.slim3scala.apt.mirror
package `type`

import java.util.Collection
import scala.collection.JavaConversions._

import com.sun.mirror.`type`.{ TypeMirror, DeclaredType, InterfaceType }

trait DeclaredTypeImpl {
  val env: AptEnv

abstract class Impl(val _type: env.global.Type)
    extends env.TypeMirror.Impl
    with DeclaredType {

  def getActualTypeArguments: Collection[TypeMirror] = {
    _type.typeArgs.map(env.typeMaker.getType)
  }

  def getSuperinterfaces: Collection[InterfaceType] = {
    _type.parents
      .filter(_.typeSymbol.isTrait)
      .map(env.typeMaker.getType(_).asInstanceOf[InterfaceType])
  }

  override val hashCode: Int = {
    41 + _type.typeSymbol.fullName.hashCode
  }

  override def toString: String = {
    _type.typeSymbol.fullName + (
      if (_type.typeArgs.nonEmpty)
        _type.typeArgs.mkString("[", ", ", "]")
      else if (_type.typeParams.nonEmpty)
        _type.typeParams.mkString("[", ", ", "]")
      else
        ""
    )
  }

  def getContainingType: DeclaredType = throw new NotImplementedException
}

}
