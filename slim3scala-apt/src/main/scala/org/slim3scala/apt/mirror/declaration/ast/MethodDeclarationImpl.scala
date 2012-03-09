package org.slim3scala.apt.mirror
package declaration.ast

import com.sun.mirror.`type`.TypeMirror

trait MethodDeclarationImpl {
  val env: AptEnv

class Impl(protected val defDef: env.global.DefDef)
    extends env.MethodDeclaration.Impl(defDef.symbol)
    with env.ast.ExecutableDeclaration.Impl {

  override def getReturnType: TypeMirror = {
    env.typeMaker.getType(defDef.tpt.tpe)
  }
}

}
