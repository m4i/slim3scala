package org.slim3scala.apt.mirror
package declaration

import java.util.Collection
import scala.collection.JavaConversions._

import com.sun.mirror.declaration.ExecutableDeclaration
import com.sun.mirror.declaration.ParameterDeclaration
import com.sun.mirror.declaration.TypeParameterDeclaration
import com.sun.mirror.`type`.ReferenceType

trait ExecutableDeclarationImpl {
  val env: AptEnv

abstract class Impl(symbol: env.global.Symbol)
    extends env.MemberDeclaration.Impl(symbol)
    with ExecutableDeclaration {

  def getParameters: Collection[ParameterDeclaration] = {
    symbol.paramss.flatten.map(env.declMaker.getParameterDeclaration)
  }

  def getFormalTypeParameters: Collection[TypeParameterDeclaration] = throw new NotImplementedException
  def getThrownTypes: Collection[ReferenceType] = throw new NotImplementedException
  def isVarArgs: Boolean = throw new NotImplementedException
}

}
