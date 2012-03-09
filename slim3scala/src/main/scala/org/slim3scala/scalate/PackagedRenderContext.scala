package org.slim3scala.scalate

import javax.servlet.ServletContext
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.fusesource.scalate.TemplateEngine
import org.fusesource.scalate.servlet.ServletRenderContext

import org.slim3scala.Constants

class PackagedRenderContext(
    engine: TemplateEngine,
    request: HttpServletRequest,
    response: HttpServletResponse,
    servletContext: ServletContext,
    rootPackageName: String)
  extends ServletRenderContext(
    engine, request, response, servletContext) {

  override protected def resolveUri(path: String): String = {
    super.resolveUri(
      if (path.startsWith("/")) prependPackage(path)
      else                      path
    )
  }

  protected def prependPackage(path: String): String = {
    "/" + rootPackageName.replace('.', '/') +
      "/" + Constants.VIEW_PACKAGE + path
  }
}
