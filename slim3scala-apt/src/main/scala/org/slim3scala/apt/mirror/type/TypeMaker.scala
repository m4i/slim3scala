package org.slim3scala.apt.mirror
package `type`

import com.sun.mirror.`type`.{ DeclaredType, TypeMirror }

trait TypeMaker {
  val env: AptEnv
  import env.global

  def getType(_type: global.Type): TypeMirror = {
    symbolToType.get(_type.typeSymbol) match {
      case Some(typeMirror) => typeMirror
      case _ =>
        _type.typeSymbol match {
          case s if s.isClass => getDeclaredType(_type)
          case _              => throw new NotImplementedException
        }
    }
  }

  def getDeclaredType(_type: global.Type): DeclaredType = {
    require(_type.typeSymbol.isClass)
    _type.typeSymbol match {
      case s if isAnnotation(s) => new env.AnnotationType.Impl(_type)
      case s if s.isTrait       => new env.InterfaceType.Impl(_type)
      case s                    => new env.ClassType.Impl(_type)
    }
  }

  protected lazy val symbolToType = {
    import com.sun.mirror.`type`.PrimitiveType.Kind._
    Map[global.Symbol, TypeMirror](
      global.definitions.BooleanClass -> new env.PrimitiveType.Impl(BOOLEAN),
      global.definitions.ByteClass    -> new env.PrimitiveType.Impl(BYTE),
      global.definitions.CharClass    -> new env.PrimitiveType.Impl(CHAR),
      global.definitions.DoubleClass  -> new env.PrimitiveType.Impl(DOUBLE),
      global.definitions.FloatClass   -> new env.PrimitiveType.Impl(FLOAT),
      global.definitions.IntClass     -> new env.PrimitiveType.Impl(INT),
      global.definitions.LongClass    -> new env.PrimitiveType.Impl(LONG),
      global.definitions.ShortClass   -> new env.PrimitiveType.Impl(SHORT),
      global.definitions.UnitClass    -> new env.VoidType.Impl
    )
  }

  protected def isAnnotation(symbol: global.Symbol): Boolean = {
    symbol.ancestors.exists(_ == global.definitions.AnnotationClass)
  }
}
