package org.slim3scala.apt.mirror
package declaration.ast

trait TypeParameterDeclarationImpl {
  val env: AptEnv

class Impl(typeDef: env.global.TypeDef)
    extends env.TypeParameterDeclaration.Impl(typeDef.symbol)
    with env.ast.Declaration.Impl {

  protected val memberDef: env.global.MemberDef = typeDef
}

}
