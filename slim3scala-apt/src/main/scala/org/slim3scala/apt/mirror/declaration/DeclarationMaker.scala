package org.slim3scala.apt.mirror
package declaration

import com.sun.mirror.declaration.AnnotationMirror
import com.sun.mirror.declaration.AnnotationValue
import com.sun.mirror.declaration.ExecutableDeclaration
import com.sun.mirror.declaration.FieldDeclaration
import com.sun.mirror.declaration.ParameterDeclaration
import com.sun.mirror.declaration.TypeDeclaration
import com.sun.mirror.declaration.TypeParameterDeclaration

trait DeclarationMaker {
  val env: AptEnv
  import env.global

  def getAnnotationMirror(annotationInfo: global.AnnotationInfo):
      AnnotationMirror = {
    new env.AnnotationMirror.Impl(annotationInfo)
  }

  def getAnnotationValue(
      arg: global.ClassfileAnnotArg, position: global.Position):
      AnnotationValue = {

    val value = arg match {
      case literal: global.LiteralAnnotArg =>
        literal.const.value match {
          case null           => null
          case v: Boolean     => java.lang.Boolean.valueOf(v)
          case v: Byte        => java.lang.Byte.valueOf(v)
          case v: Char        => java.lang.Character.valueOf(v)
          case v: Double      => java.lang.Double.valueOf(v)
          case v: Float       => java.lang.Float.valueOf(v)
          case v: Int         => java.lang.Integer.valueOf(v)
          case v: Long        => java.lang.Long.valueOf(v)
          case v: Short       => java.lang.Short.valueOf(v)
          case v: String      => v
          case v: global.Type => env.typeMaker.getType(v)
          case _              => throw new NotImplementedException
        }
      case _ => throw new NotImplementedException
    }
    new env.AnnotationValue.Impl(value, position)
  }

  def getAnnotationValue(_value: Any): AnnotationValue = {
    val value = _value match {
      case null        => null
      case v: Boolean  => java.lang.Boolean.valueOf(v)
      case v: Byte     => java.lang.Byte.valueOf(v)
      case v: Char     => java.lang.Character.valueOf(v)
      case v: Double   => java.lang.Double.valueOf(v)
      case v: Float    => java.lang.Float.valueOf(v)
      case v: Int      => java.lang.Integer.valueOf(v)
      case v: Long     => java.lang.Long.valueOf(v)
      case v: Short    => java.lang.Short.valueOf(v)
      case v: String   => v
      case v: Class[_] => throw new NotImplementedException
      case _           => throw new NotImplementedException
    }
    new env.AnnotationValue.Impl(value, null)
  }

  def getExecutableDeclaration(symbol: global.Symbol):
      ExecutableDeclaration = {
    require(symbol.isMethod)
    symbol match {
      case s if s.isConstructor =>
        new env.ConstructorDeclaration.Impl(s)
      case s if isAnnotation(s.owner) =>
        new env.AnnotationTypeElementDeclaration.Impl(s)
      case s =>
        new env.MethodDeclaration.Impl(s)
    }
  }

  def getExecutableDeclaration(defDef: global.DefDef):
      ExecutableDeclaration = {
    defDef match {
      case d if d.symbol.isConstructor =>
        new env.ast.ConstructorDeclaration.Impl(d)
      case d if isAnnotation(d.symbol.owner) =>
        //new env.ast.AnnotationTypeElementDeclaration.Impl(d)
        throw new NotImplementedException
      case d =>
        new env.ast.MethodDeclaration.Impl(d)
    }
  }

  def getFieldDeclaration(symbol: global.Symbol): FieldDeclaration = {
    require(symbol.isTerm && !symbol.isMethod)
    new env.FieldDeclaration.Impl(symbol)
  }

  def getFieldDeclaration(valDef: global.ValDef): FieldDeclaration = {
    new env.ast.FieldDeclaration.Impl(valDef)
  }

  def getParameterDeclaration(symbol: global.Symbol):
      ParameterDeclaration = {
    require(symbol.isParameter)
    new env.ParameterDeclaration.Impl(symbol)
  }

  def getParameterDeclaration(valDef: global.ValDef):
      ParameterDeclaration = {
    new env.ast.ParameterDeclaration.Impl(valDef)
  }

  def getTypeDeclaration(symbol: global.Symbol): TypeDeclaration = {
    require(symbol.isClass)
    symbol match {
      case s if isAnnotation(s) => new env.AnnotationTypeDeclaration.Impl(s)
      case s if s.isTrait       => new env.InterfaceDeclaration.Impl(s)
      case s                    => new env.ClassDeclaration.Impl(s)
    }
  }

  def getTypeDeclaration(name: String): TypeDeclaration = {
    getTypeDeclaration(env.getSymbol(name))
  }

  def getTypeDeclaration(classDef: global.ClassDef): TypeDeclaration = {
    classDef match {
      case d if isAnnotation(d) =>
        //new env.ast.AnnotationTypeDeclaration.Impl(d)
        throw new NotImplementedException
      case d if d.keyword == "trait" =>
        //new env.ast.InterfaceDeclaration.Impl(d)
        throw new NotImplementedException
      case d =>
        new env.ast.ClassDeclaration.Impl(d)
    }
  }

  def getTypeParameterDeclaration(symbol: global.Symbol):
      TypeParameterDeclaration = {
    require(symbol.isTypeParameter)
    new env.TypeParameterDeclaration.Impl(symbol)
  }

  def getTypeParameterDeclaration(typeDef: global.TypeDef):
      TypeParameterDeclaration = {
    new env.ast.TypeParameterDeclaration.Impl(typeDef)
  }

  protected def isAnnotation(symbol: global.Symbol): Boolean = {
    symbol.ancestors.exists(_ == global.definitions.AnnotationClass)
  }

  protected def isAnnotation(classDef: global.ClassDef): Boolean = {
    classDef.impl.parents.exists(_.symbol == global.definitions.AnnotationClass)
  }
}
