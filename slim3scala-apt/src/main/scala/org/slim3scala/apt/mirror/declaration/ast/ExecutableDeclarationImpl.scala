package org.slim3scala.apt.mirror
package declaration.ast

import java.util.Collection
import scala.collection.JavaConversions._

import com.sun.mirror.declaration.ParameterDeclaration

trait ExecutableDeclarationImpl {
  val env: AptEnv

trait Impl
    extends env.ExecutableDeclaration.Impl
    with env.ast.Declaration.Impl {

  protected val defDef: env.global.DefDef
  protected val memberDef: env.global.MemberDef = defDef

  override def getParameters: Collection[ParameterDeclaration] = {
    defDef.vparamss.flatten.map(env.declMaker.getParameterDeclaration)
  }
}

}
