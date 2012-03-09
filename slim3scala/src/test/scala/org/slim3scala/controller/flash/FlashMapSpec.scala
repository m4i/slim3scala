package org.slim3scala.controller.flash

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers

class FlashMapSpec extends WordSpec with ShouldMatchers {
  "A FlashMap" should {
    "store and get" in {
      val flash = new FlashMap
      flash("foo") = 1
      flash.keySet should equal (Set("foo"))
      flash[Int]("foo") should equal (Some(1))
    }

    "not sweep kept keys" in {
      val flash = new FlashMap
      flash("foo") = 1
      flash.sweep()
      flash.keySet should equal (Set("foo"))
    }

    "sweep keys at next access" in {
      val flash = new FlashMap
      flash("foo") = 1
      flash.sweep()
      val nextFlash = new FlashMap(flash.store)
      nextFlash.keySet should equal (Set("foo"))
      nextFlash.sweep()
      nextFlash.keySet should equal (Set())
    }
  }
}
