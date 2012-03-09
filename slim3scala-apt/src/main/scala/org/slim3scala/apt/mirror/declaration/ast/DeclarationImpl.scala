package org.slim3scala.apt.mirror
package declaration.ast

import java.util.Collection
import java.util.EnumSet

import com.sun.mirror.declaration.Modifier
import com.sun.mirror.util.SourcePosition

trait DeclarationImpl {
  val env: AptEnv

trait Impl
    extends env.Declaration.Impl {

  protected val memberDef: env.global.MemberDef

  override lazy val getModifiers: Collection[Modifier] = {
    import Modifier._
    val modifiers = EnumSet.noneOf(classOf[Modifier])
    if (memberDef.mods.isAbstract)  modifiers.add(ABSTRACT)
    if (memberDef.mods.isFinal)     modifiers.add(FINAL)
    if (memberDef.mods.isPrivate)   modifiers.add(PRIVATE)
    if (memberDef.mods.isProtected) modifiers.add(PROTECTED)
    if (memberDef.mods.isPublic)    modifiers.add(PUBLIC)
    modifiers
  }

  override def getPosition: SourcePosition = {
    new util.SourcePositionImpl(memberDef.pos)
  }
}

}
