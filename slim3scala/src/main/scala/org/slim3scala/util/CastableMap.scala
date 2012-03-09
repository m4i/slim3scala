package org.slim3scala.util

import scala.collection.mutable.{ Map => MMap }

class CastableMap(val store: MMap[String, Any]) extends MMap[String, Any] {
  if (store == null) {
    throw new NullPointerException("The store parameter is null.")
  }

  def apply[T](key: String)(implicit m: Manifest[T]): Option[T] = {
    get(key) match {
      case Some(value) =>
        Some(ScalaUtil.cast[T](value))
      case _ =>
        None
    }
  }

  def +=(kv: (String, Any)) = { store += kv; this }
  def -=(key: String) = { store -= key; this }
  def get(key: String) = store.get(key)
  def iterator = store.iterator
}
