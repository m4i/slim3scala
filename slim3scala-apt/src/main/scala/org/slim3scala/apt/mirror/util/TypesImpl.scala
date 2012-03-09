package org.slim3scala.apt.mirror
package util

import java.util.Collection

import com.sun.mirror.declaration.TypeDeclaration
import com.sun.mirror.declaration.TypeParameterDeclaration
import com.sun.mirror.`type`.ArrayType
import com.sun.mirror.`type`.DeclaredType
import com.sun.mirror.`type`.PrimitiveType
import com.sun.mirror.`type`.ReferenceType
import com.sun.mirror.`type`.TypeMirror
import com.sun.mirror.`type`.TypeVariable
import com.sun.mirror.`type`.VoidType
import com.sun.mirror.`type`.WildcardType
import com.sun.mirror.util.Types

trait TypesImpl extends Types {
  val env: AptEnv

  def getErasure(t: TypeMirror): TypeMirror = {
    t match {
      case t: env.DeclaredType.Impl =>
        env.typeMaker.getType(t._type.typeSymbol.info)
      case t: PrimitiveType => t
      case t: VoidType      => t
      case _                => throw new NotImplementedException
    }
  }

  def getDeclaredType(decl: TypeDeclaration, typeArgs: TypeMirror*): DeclaredType = {
    val symbol = decl.asInstanceOf[env.TypeDeclaration.Impl].symbol

    if (typeArgs.isEmpty) {
      env.typeMaker.getDeclaredType(symbol.info)
    } else {
      throw new NotImplementedException
    }
  }

  def isSubtype(t1: TypeMirror, t2: TypeMirror): Boolean = {
    t1 match {
      case t1: env.DeclaredType.Impl =>
        t2 match {
          case t2: env.DeclaredType.Impl =>
            t1._type match {
              case _type1: env.global.PolyType =>
                t2._type match {
                  case _type2: env.global.PolyType =>
                    _type1.parents.exists(
                      _.asInstanceOf[env.global.TypeRef].thisInfo == _type2)
                  case _ => throw new NotImplementedException
                }
              case _ => throw new NotImplementedException
            }
          case _ => throw new NotImplementedException
        }
      case _ => throw new NotImplementedException
    }
  }

  def getArrayType(componentType: TypeMirror): ArrayType = throw new NotImplementedException
  def getDeclaredType(containing: DeclaredType, decl: TypeDeclaration, typeArgs: TypeMirror*): DeclaredType = throw new NotImplementedException
  def getPrimitiveType(kind: PrimitiveType.Kind): PrimitiveType = throw new NotImplementedException
  def getTypeVariable(tparam: TypeParameterDeclaration): TypeVariable = throw new NotImplementedException
  def getVoidType: VoidType = throw new NotImplementedException
  def getWildcardType(upperBounds: Collection[ReferenceType], lowerBounds: Collection[ReferenceType]): WildcardType = throw new NotImplementedException
  def isAssignable(t1: TypeMirror, t2: TypeMirror): Boolean = throw new NotImplementedException
}
