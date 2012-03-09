package org.slim3scala.apt.mirror
package declaration

import java.util.Collection
import scala.collection.JavaConversions._

import com.sun.mirror.declaration.ClassDeclaration
import com.sun.mirror.declaration.ConstructorDeclaration
import com.sun.mirror.declaration.MethodDeclaration
import com.sun.mirror.`type`.ClassType

trait ClassDeclarationImpl {
  val env: AptEnv

class Impl(symbol: env.global.Symbol)
    extends env.TypeDeclaration.Impl(symbol)
    with ClassDeclaration {

  def getConstructors: Collection[ConstructorDeclaration] = {
    symbol.info.decls.collect {
      case s: env.global.MethodSymbol if s.isConstructor =>
        env.declMaker.getExecutableDeclaration(s)
          .asInstanceOf[ConstructorDeclaration]
    }
  }

  def getMethods: Collection[MethodDeclaration] = {
    getMethodSymbols.map(
      env.declMaker.getExecutableDeclaration(_)
      .asInstanceOf[MethodDeclaration])
  }

  def getSuperclass: ClassType = {
    env.typeMaker.getType(symbol.superClass.info).asInstanceOf[ClassType]
  }

  def canEqual(other: Any): Boolean = {
    other.isInstanceOf[Impl]
  }

  override def equals(other: Any): Boolean = {
    other match {
      case that: Impl =>
        (that canEqual this) &&
        getQualifiedName == that.getQualifiedName
      case _ => false
    }
  }
}

}
