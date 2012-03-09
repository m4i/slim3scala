package org.slim3scala.apt.mirror
package declaration.ast

import java.util.Collection
import scala.collection.JavaConversions._

import com.sun.mirror.declaration.FieldDeclaration
import com.sun.mirror.declaration.TypeParameterDeclaration
import com.sun.mirror.`type`.InterfaceType

trait TypeDeclarationImpl {
  val env: AptEnv

trait Impl
    extends env.TypeDeclaration.Impl
    with env.ast.Declaration.Impl {

  protected val classDef: env.global.ClassDef
  protected val memberDef: env.global.MemberDef = classDef

  override def getFields: Collection[FieldDeclaration] = {
    classDef.impl.body.collect {
      case d: env.global.ValDef => env.declMaker.getFieldDeclaration(d)
    }
  }

  override def getFormalTypeParameters: Collection[TypeParameterDeclaration] = {
    classDef.tparams.map(env.declMaker.getTypeParameterDeclaration)
  }

  override def getSuperinterfaces: Collection[InterfaceType] = {
    classDef.impl.parents
      .filter(_.symbol.isTrait)
      .map(d =>
        env.typeMaker.getType(d.symbol.info).asInstanceOf[InterfaceType])
  }

  protected def getMethodDefs: List[env.global.DefDef] = {
    classDef.impl.body.collect {
      case d: env.global.DefDef if !d.symbol.isConstructor => d
    }
  }
}

}
