package org.slim3scala.gen.util

object ScalaUtil {
  protected val primitiveToReference = {
    import org.slim3.gen.ClassConstants._
    Map(
      primitive_char    -> Character,
      primitive_byte    -> Byte,
      primitive_boolean -> Boolean,
      primitive_short   -> Short,
      primitive_int     -> Integer,
      primitive_long    -> Long,
      primitive_float   -> Float,
      primitive_double  -> Double
    )
  }

  def getReferenceClassName(className: String) = {
    primitiveToReference.get(className).getOrElse(className)
  }
}
