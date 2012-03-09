package org.slim3scala.apt.mirror
package declaration

import java.util.Collection
import scala.collection.JavaConversions._

import com.sun.mirror.declaration.FieldDeclaration
import com.sun.mirror.declaration.PackageDeclaration
import com.sun.mirror.declaration.TypeDeclaration
import com.sun.mirror.declaration.TypeParameterDeclaration
import com.sun.mirror.`type`.InterfaceType

trait TypeDeclarationImpl {
  val env: AptEnv

abstract class Impl(symbol: env.global.Symbol)
    extends env.MemberDeclaration.Impl(symbol)
    with TypeDeclaration {

  def getFields: Collection[FieldDeclaration] = {
    symbol.info.decls.collect {
      case s: env.global.TermSymbol if !s.isMethod =>
        env.declMaker.getFieldDeclaration(s)
    }
  }

  def getFormalTypeParameters: Collection[TypeParameterDeclaration] = {
    symbol.typeParams.map(env.declMaker.getTypeParameterDeclaration)
  }

  def getQualifiedName: String = {
    symbol.fullName
  }

  def getSuperinterfaces: Collection[InterfaceType] = {
    symbol.info.parents
      .filter(_.typeSymbol.isTrait)
      .map(env.typeMaker.getType(_).asInstanceOf[InterfaceType])
  }

  protected def getMethodSymbols: Iterable[env.global.MethodSymbol] = {
    symbol.info.decls.collect {
      case s: env.global.MethodSymbol if !s.isConstructor => s
    }
  }

  override val hashCode: Int = {
    41 + getQualifiedName.hashCode
  }

  def getNestedTypes: Collection[TypeDeclaration] = throw new NotImplementedException
  def getPackage: PackageDeclaration = throw new NotImplementedException
}

}
