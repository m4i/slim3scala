package org.slim3scala.controller.flash

import scala.collection.mutable.{ Map => MMap }
import scala.collection.mutable.{ Set => MSet }

import org.slim3scala.util.CastableMap

class FlashMap(store: MMap[String, Any] = MMap[String, Any]())
  extends CastableMap(store) with FlashIdiom  {

  lazy val now: FlashNow = new FlashNow(this)

  protected val keepKeys = MSet[String]()

  override def +=(kv: (String, Any)) = {
    keep(kv._1)
    super.+=(kv)
  }

  def keep() {
    keepKeys ++= keys
  }

  def keep(key: String) {
    keepKeys += key
  }

  def discard() {
    keepKeys.clear()
  }

  def discard(key: String) {
    keepKeys -= key
  }

  def sweep() {
    keys.filterNot(keepKeys).foreach(this -=)
  }

  override def toString: String = {
    "FlashMap" + store.toString.dropWhile('(' !=)
  }
}
