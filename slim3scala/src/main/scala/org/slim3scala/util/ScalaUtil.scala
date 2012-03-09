package org.slim3scala.util

object ScalaUtil {
  protected val boxingMap = Map[Class[_], Class[_]](
    classOf[Boolean] -> classOf[java.lang.Boolean],
    classOf[Byte]    -> classOf[java.lang.Byte],
    classOf[Char]    -> classOf[java.lang.Character],
    classOf[Double]  -> classOf[java.lang.Double],
    classOf[Float]   -> classOf[java.lang.Float],
    classOf[Int]     -> classOf[java.lang.Integer],
    classOf[Long]    -> classOf[java.lang.Long],
    classOf[Short]   -> classOf[java.lang.Short],
    classOf[Unit]    -> classOf[scala.runtime.BoxedUnit]
  )

  def cast[T](any: Any)(implicit m: Manifest[T]): T = {
    if (m.erasure == classOf[AnyRef]) {
      throw new IllegalArgumentException("Require explicit type.")
    }
    (
      boxingMap.get(m.erasure) match {
        case Some(clazz) => clazz
        case _           => m.erasure
      }
    ).cast(any).asInstanceOf[T]
  }
}
