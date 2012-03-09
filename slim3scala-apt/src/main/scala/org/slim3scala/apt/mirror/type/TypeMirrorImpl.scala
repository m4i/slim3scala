package org.slim3scala.apt.mirror
package `type`

import com.sun.mirror.`type`.TypeMirror

trait TypeMirrorImpl {
  val env: AptEnv

abstract class Impl
    extends TypeMirror {

  override def toString: String = throw new NotImplementedException
}

}
