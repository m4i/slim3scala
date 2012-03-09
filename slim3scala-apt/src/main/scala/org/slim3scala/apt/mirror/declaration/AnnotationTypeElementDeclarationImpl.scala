package org.slim3scala.apt.mirror
package declaration

import com.sun.mirror.declaration.AnnotationTypeDeclaration
import com.sun.mirror.declaration.AnnotationTypeElementDeclaration
import com.sun.mirror.declaration.AnnotationValue
import com.sun.mirror.`type`.TypeMirror

trait AnnotationTypeElementDeclarationImpl {
  val env: AptEnv

class Impl(symbol: env.global.Symbol)
    extends env.MethodDeclaration.Impl(symbol)
    with AnnotationTypeElementDeclaration {

  override def getDeclaringType: AnnotationTypeDeclaration = {
    super.getDeclaringType.asInstanceOf[AnnotationTypeDeclaration]
  }

  lazy val getDefaultValue: AnnotationValue = {
    val clazz = Class.forName(symbol.owner.fullName)
    val method = clazz.getMethod(symbol.nameString)
    env.declMaker.getAnnotationValue(method.getDefaultValue)
  }

  def canEqual(other: Any): Boolean = {
    other.isInstanceOf[Impl]
  }

  override def equals(other: Any): Boolean = {
    other match {
      case that: Impl =>
        (that canEqual this) &&
        symbol == that.symbol
      case _ => false
    }
  }

  override val hashCode: Int = {
    41 + symbol.hashCode
  }
}

}
