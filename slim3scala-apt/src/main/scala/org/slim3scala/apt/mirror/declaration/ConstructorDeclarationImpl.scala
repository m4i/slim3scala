package org.slim3scala.apt.mirror
package declaration

import com.sun.mirror.declaration.ConstructorDeclaration

trait ConstructorDeclarationImpl {
  val env: AptEnv

class Impl(symbol: env.global.Symbol)
    extends env.ExecutableDeclaration.Impl(symbol)
    with ConstructorDeclaration

}
