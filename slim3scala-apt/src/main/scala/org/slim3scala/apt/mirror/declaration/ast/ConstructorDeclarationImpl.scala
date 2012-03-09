package org.slim3scala.apt.mirror
package declaration.ast

trait ConstructorDeclarationImpl {
  val env: AptEnv

class Impl(protected val defDef: env.global.DefDef)
    extends env.ConstructorDeclaration.Impl(defDef.symbol)
    with env.ast.ExecutableDeclaration.Impl

}
