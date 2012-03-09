package org.slim3scala.controller.flash

import org.slim3scala.Constants
import org.slim3scala.util.CastableMap

trait FlashIdiom extends CastableMap {
  def alert: Option[String] = {
    apply[String](Constants.FLASH_ALERT_KEY)
  }

  def alert_=(message: String) {
    update(Constants.FLASH_ALERT_KEY, message)
  }

  def notice: Option[String] = {
    apply[String](Constants.FLASH_NOTICE_KEY)
  }

  def notice_=(message: String) {
    update(Constants.FLASH_NOTICE_KEY, message)
  }
}
