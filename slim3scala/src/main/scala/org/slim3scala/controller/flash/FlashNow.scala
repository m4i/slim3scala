package org.slim3scala.controller.flash

import org.slim3scala.util.CastableMap

class FlashNow(flash: FlashMap)
  extends CastableMap(flash.store) with FlashIdiom
