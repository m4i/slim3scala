package org.slim3scala.apt.mirror
package declaration

import java.util.Collection

import com.sun.mirror.declaration.{ Declaration, TypeParameterDeclaration }
import com.sun.mirror.`type`.ReferenceType

trait TypeParameterDeclarationImpl {
  val env: AptEnv

class Impl(symbol: env.global.Symbol)
    extends env.Declaration.Impl(symbol)
    with TypeParameterDeclaration {

  def getBounds: Collection[ReferenceType] = throw new NotImplementedException
  def getOwner: Declaration = throw new NotImplementedException
}

}
