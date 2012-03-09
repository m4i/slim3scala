package org.slim3scala.apt.mirror
package declaration

import com.sun.mirror.declaration.AnnotationValue
import com.sun.mirror.util.SourcePosition

trait AnnotationValueImpl {
  val env: AptEnv

class Impl(value: Object, position: env.global.Position)
    extends AnnotationValue {

  def getPosition: SourcePosition = {
    if (position == null) null
    else new util.SourcePositionImpl(position)
  }

  def getValue: Object = {
    value
  }

  override def toString = throw new NotImplementedException
}

}
