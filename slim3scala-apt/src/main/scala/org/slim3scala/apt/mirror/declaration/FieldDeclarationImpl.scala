package org.slim3scala.apt.mirror
package declaration

import com.sun.mirror.declaration.FieldDeclaration
import com.sun.mirror.`type`.TypeMirror

trait FieldDeclarationImpl {
  val env: AptEnv

class Impl(symbol: env.global.Symbol)
    extends env.MemberDeclaration.Impl(symbol)
    with FieldDeclaration {

  def getType: TypeMirror = {
    env.typeMaker.getType(symbol.tpe)
  }

  def getConstantExpression: String = throw new NotImplementedException
  def getConstantValue: Object = throw new NotImplementedException
}

}
