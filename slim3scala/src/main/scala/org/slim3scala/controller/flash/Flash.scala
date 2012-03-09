package org.slim3scala.controller.flash

import scala.collection.mutable.{ Map => MMap }

import org.slim3scala.Constants
import org.slim3scala.controller.Controller

/**
 * Require using org.slim3scala.controller.FatFrontController
 * instead of org.slim3.controller.FrontController.
 *
 * @todo extends org.slim3.controller.Controller
 *       but trait cannot access java protected method requestScope/sessionScope
 */
trait Flash extends Controller {
  lazy val flash: FlashMap = {
    val store = sessionScope[MMap[String, Any]](Constants.FLASH_KEY)
    val flash =
      if (store == null) {
        val flash = new FlashMap
        sessionScope(Constants.FLASH_KEY, flash.store)
        flash
      } else {
        new FlashMap(store)
      }
    requestScope(Constants.FLASH_KEY, flash)
    flash
  }
}
