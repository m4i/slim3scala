package org.slim3scala.apt.mirror
package declaration

import com.sun.mirror.declaration.{ MemberDeclaration, TypeDeclaration }

trait MemberDeclarationImpl {
  val env: AptEnv

abstract class Impl(symbol: env.global.Symbol)
    extends env.Declaration.Impl(symbol)
    with MemberDeclaration {

  def getDeclaringType: TypeDeclaration = {
    symbol.owner match {
      case s: env.global.ClassSymbol if !s.isPackageClass =>
        env.declMaker.getTypeDeclaration(s)
      case _ =>
        null
    }
  }
}

}
