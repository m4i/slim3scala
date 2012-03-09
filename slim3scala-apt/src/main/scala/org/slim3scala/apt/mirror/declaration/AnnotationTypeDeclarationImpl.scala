package org.slim3scala.apt.mirror
package declaration

import java.util.Collection
import scala.collection.JavaConversions._

import com.sun.mirror.declaration.AnnotationTypeDeclaration
import com.sun.mirror.declaration.AnnotationTypeElementDeclaration

trait AnnotationTypeDeclarationImpl {
  val env: AptEnv

class Impl(symbol: env.global.Symbol)
    extends env.TypeDeclaration.Impl(symbol)
    with AnnotationTypeDeclaration {

  override def getMethods: Collection[AnnotationTypeElementDeclaration] = {
    getMethodSymbols.map(
      env.declMaker.getExecutableDeclaration(_)
      .asInstanceOf[AnnotationTypeElementDeclaration])
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
