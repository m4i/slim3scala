package org.slim3scala.apt.mirror
package declaration

import com.sun.mirror.declaration.MethodDeclaration
import com.sun.mirror.`type`.TypeMirror

trait MethodDeclarationImpl {
  val env: AptEnv

class Impl(symbol: env.global.Symbol)
    extends env.ExecutableDeclaration.Impl(symbol)
    with MethodDeclaration {

  def getReturnType: TypeMirror = {
    env.typeMaker.getType(symbol.tpe.resultType)
  }
}

}
