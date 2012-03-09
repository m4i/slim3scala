package org.slim3scala.view

import org.slim3scala.Constants

abstract class Template(view: View) {
  protected val path: String
  def render(): Any

  protected lazy val basePath: String = path.take(path.lastIndexOf('/') + 1)

  def render(path: String): Any = {
    view.renderAsAny(toAbsolutePath(path))
  }

  protected def servletContext = view.servletContext
  protected def request        = view.request
  protected def response       = view.response
  protected def controller     = view.controller
  protected def contentFor     = view.contentFor

  protected def contentForLayout: Any = {
    contentFor(Constants.LAYOUT_KEY)
  }

  protected def layout: String = {
    request.getAttribute(Constants.LAYOUT_KEY).asInstanceOf[String]
  }

  protected def layout_=(path: String) {
    request.setAttribute(Constants.LAYOUT_KEY, path)
  }

  protected def toAbsolutePath(path: String): String = {
    if (path.startsWith("/")) path
    else                      basePath + path
  }
}
