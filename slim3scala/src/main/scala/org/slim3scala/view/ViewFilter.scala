package org.slim3scala.view

import scala.collection.mutable
import scala.util.DynamicVariable

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletContext
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.slim3.controller.Controller
import org.slim3.controller.ControllerConstants
import org.slim3.util.AppEngineUtil
import org.slim3.util.RequestUtil

class ViewFilter extends Filter {
  protected val charset = "utf-8"

  protected val contentTypes = Map(
    "html"  -> "text/html",
    "xhtml" -> "text/html",
    "text"  -> "text/plain",
    "txt"   -> "text/plain",
    "js"    -> "text/javascript",
    "css"   -> "text/css",
    "ics"   -> "text/calendar",
    "csv"   -> "text/csv",
    "xml"   -> "application/xml",
    "rss"   -> "application/rss+xml",
    "atom"  -> "application/atom+xml",
    "yaml"  -> "application/x-yaml",
    "json"  -> "application/json"
  )

  protected val fallbackContentType = "text/html"

  protected var servletContext: ServletContext = _

  protected val request  = new DynamicVariable[HttpServletRequest](null)
  protected val response = new DynamicVariable[HttpServletResponse](null)

  protected val negativeCache = mutable.Set[String]()

  protected lazy val hotReloading =
    AppEngineUtil.isDevelopment &&
    !"false".equalsIgnoreCase(
      System.getProperty(ControllerConstants.HOT_RELOADING_KEY))

  protected lazy val rootPackageName =
    servletContext.getInitParameter(ControllerConstants.ROOT_PACKAGE_KEY)

  def init(filterConfig: FilterConfig) {
    servletContext = filterConfig.getServletContext
  }

  def destroy() {}

  def doFilter(
      request: ServletRequest,
      response: ServletResponse,
      chain: FilterChain) {
    (request, response) match {
      case (request: HttpServletRequest, response: HttpServletResponse) =>
        val path = RequestUtil.getPath(request)
        if (isRenderable(path)) {
          try {
            this.request.withValue(request) {
              this.response.withValue(response) {
                renderWithContentType(path)
              }
            }
            return
          } catch {
            case e: TemplateNotFoundException =>
              if (e.path == path) {
                negativeCache += path
              } else {
                throw e
              }
          }
        }
      case _ =>
    }
    chain.doFilter(request, response)
  }

  protected def isRenderable(path: String): Boolean = {
    (hotReloading || !negativeCache(path)) &&
    !path.startsWith("/_ah/")
  }

  protected def renderWithContentType(path: String) {
    if (response.value.getContentType == null) {
      response.value.setContentType(getContentType(path))
    }
    render(path)
  }

  protected def getContentType(path: String): String = {
    val contentType = contentTypes.get(getContentExtension(path)) match {
      case Some(contentType) => contentType
      case _                 => fallbackContentType
    }
    contentType + "; charset=" + charset
  }

  protected def getContentExtension(path: String): String = {
    RequestUtil.getExtension(path)
  }

  protected def getController: Controller = {
    request.value.getAttribute(
      ControllerConstants.CONTROLLER_KEY).asInstanceOf[Controller]
  }

  protected def render(path: String) {
    render(createView(), path)
  }

  protected def render(view: View, path: String) {
    response.value.getWriter.write(view.render(path))
  }

  protected def createView(): View = {
    new View(servletContext,
      request.value, response.value, getController, rootPackageName)
  }
}
