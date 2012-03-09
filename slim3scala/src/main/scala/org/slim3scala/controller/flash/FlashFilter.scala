package org.slim3scala.controller.flash

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.slim3.controller.FrontController

import org.slim3scala.Constants

trait FlashFilter extends FrontController {
  override protected def doFilter(
      request: HttpServletRequest,
      response: HttpServletResponse,
      chain: FilterChain) {
    try {
      super.doFilter(request, response, chain)
    } finally {
      sweepFlash(request)
    }
  }

  protected def sweepFlash(request: HttpServletRequest) {
    val flash = request
      .getAttribute(Constants.FLASH_KEY).asInstanceOf[FlashMap]

    if (flash != null) {
      flash.sweep()
    }

    if (flash == null || flash.isEmpty) {
      val session = request.getSession(false)
      if (session != null) {
        session.removeAttribute(Constants.FLASH_KEY)
      }
    }
  }
}
