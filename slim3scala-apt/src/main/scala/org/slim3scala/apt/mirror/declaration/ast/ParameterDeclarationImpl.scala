package org.slim3scala.apt.mirror
package declaration.ast

import com.sun.mirror.`type`.TypeMirror

trait ParameterDeclarationImpl {
  val env: AptEnv

class Impl(valDef: env.global.ValDef)
    extends env.ParameterDeclaration.Impl(valDef.symbol)
    with env.ast.Declaration.Impl {

  protected val memberDef: env.global.MemberDef = valDef

  override def getType: TypeMirror = {
    env.typeMaker.getType(valDef.tpt.tpe)
  }
}

}
