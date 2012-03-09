package org.slim3scala.apt.mirror
package declaration

import com.sun.mirror.declaration.ParameterDeclaration
import com.sun.mirror.`type`.TypeMirror

trait ParameterDeclarationImpl {
  val env: AptEnv

class Impl(symbol: env.global.Symbol)
    extends env.Declaration.Impl(symbol)
    with ParameterDeclaration {

  def getType: TypeMirror = {
    env.typeMaker.getType(symbol.tpe)
  }
}

}
