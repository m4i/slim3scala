package org.slim3scala.apt.mirror
package declaration.ast

import java.util.Collection
import scala.collection.JavaConversions._

import com.sun.mirror.declaration.ConstructorDeclaration
import com.sun.mirror.declaration.MethodDeclaration
import com.sun.mirror.`type`.ClassType

trait ClassDeclarationImpl {
  val env: AptEnv

class Impl(protected val classDef: env.global.ClassDef)
    extends env.ClassDeclaration.Impl(classDef.symbol)
    with env.ast.TypeDeclaration.Impl {

  override def getConstructors: Collection[ConstructorDeclaration] = {
    classDef.impl.body.collect {
      case d: env.global.DefDef if d.symbol.isConstructor =>
        env.declMaker.getExecutableDeclaration(d)
          .asInstanceOf[ConstructorDeclaration]
    }
  }

  override def getMethods: Collection[MethodDeclaration] = {
    getMethodDefs.map(
      env.declMaker.getExecutableDeclaration(_)
      .asInstanceOf[MethodDeclaration])
  }

  override def getSuperclass: ClassType = {
    env.typeMaker.getType(classDef.impl.parents.head.symbol.info)
      .asInstanceOf[ClassType]
  }
}

}
