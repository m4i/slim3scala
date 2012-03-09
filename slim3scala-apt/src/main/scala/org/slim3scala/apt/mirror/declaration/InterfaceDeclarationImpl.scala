package org.slim3scala.apt.mirror
package declaration

import java.util.Collection
import scala.collection.JavaConversions._

import com.sun.mirror.declaration.{ InterfaceDeclaration, MethodDeclaration }

trait InterfaceDeclarationImpl {
  val env: AptEnv

class Impl(symbol: env.global.Symbol)
    extends env.TypeDeclaration.Impl(symbol)
    with InterfaceDeclaration {

  def getMethods: Collection[_ <: MethodDeclaration] = {
    getMethodSymbols.map(
      env.declMaker.getExecutableDeclaration(_)
      .asInstanceOf[MethodDeclaration])
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
