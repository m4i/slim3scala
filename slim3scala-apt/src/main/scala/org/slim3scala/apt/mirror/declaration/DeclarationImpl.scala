package org.slim3scala.apt.mirror
package declaration

import java.lang.annotation.Annotation
import java.util.Collection
import java.util.EnumSet
import scala.collection.JavaConversions._

import com.sun.mirror.declaration.{ AnnotationMirror, Declaration, Modifier }
import com.sun.mirror.util.{ DeclarationVisitor, SourcePosition }

trait DeclarationImpl {
  val env: AptEnv

abstract class Impl(val symbol: env.global.Symbol)
    extends Declaration {

  def getAnnotationMirrors: Collection[AnnotationMirror] = {
    symbol.annotations.map(env.declMaker.getAnnotationMirror)
  }

  lazy val getModifiers: Collection[Modifier] = {
    import Modifier._
    val modifiers = EnumSet.noneOf(classOf[Modifier])
    if (symbol.isAbstractClass) modifiers.add(ABSTRACT)
    if (symbol.isFinal)         modifiers.add(FINAL)
    if (symbol.isPrivate)       modifiers.add(PRIVATE)
    if (symbol.isProtected)     modifiers.add(PROTECTED)
    if (symbol.isPublic)        modifiers.add(PUBLIC)
    if (symbol.isStatic)        modifiers.add(STATIC)
    modifiers
  }

  def getPosition: SourcePosition = {
    new util.SourcePositionImpl(symbol.pos)
  }

  def getSimpleName: String = {
    symbol.nameString
  }

  def accept(v: DeclarationVisitor) { throw new NotImplementedException }
  def getAnnotation[A <: Annotation](annotationType: Class[A]): A = throw new NotImplementedException
  def getDocComment: String = throw new NotImplementedException
  override def equals(other: Any): Boolean = throw new NotImplementedException
  override def hashCode: Int = throw new NotImplementedException
}

}
