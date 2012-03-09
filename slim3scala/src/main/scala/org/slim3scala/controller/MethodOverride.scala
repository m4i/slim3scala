package org.slim3scala.controller

import org.slim3.util.RequestLocator

/**
 * @todo extends org.slim3.controller.Controller
 *       but trait cannot access java protected method asString
 */
trait MethodOverride extends Controller {
  protected[this] val METHOD_OVERRIDE_HEADER = "HTTP_X_HTTP_METHOD_OVERRIDE"
  protected[this] val METHOD_OVERRIDE_KEY    = "_method"
  protected[this] val OVERRIDABLE_METHODS    = Set("GET", "POST", "PUT", "DELETE")

  protected[this] lazy val overriddenMethod: String = {
    // trait cannot access java protected variable request
    val request = RequestLocator.get

    def getParameterMethod: Option[String] = {
      var method = asString(METHOD_OVERRIDE_KEY)
      if (method != null) {
        method = method.toUpperCase
        if (OVERRIDABLE_METHODS(method)) {
          if (request.getQueryString == null) {
            return Some(method)
          } else {
            "(?:^|&)%s=%s(?:&|$)".format(METHOD_OVERRIDE_KEY, method).r
              .findFirstIn(request.getQueryString) match {
                case None =>
                  return Some(method)
                case _ =>
              }
          }
        }
      }
      None
    }

    def getHeaderMethod: Option[String] = {
      var method = request.getHeader(METHOD_OVERRIDE_HEADER)
      if (method != null) {
        method = method.toUpperCase
        if (OVERRIDABLE_METHODS(method)) {
          return Some(method)
        }
      }
      None
    }

    val method = request.getMethod
    if (method == "POST") {
      getParameterMethod.orElse(getHeaderMethod).getOrElse(method)
    } else {
      method
    }
  }

  override protected def isGet: Boolean = {
    overriddenMethod == "GET"
  }

  override protected def isPost: Boolean = {
    overriddenMethod == "POST"
  }

  override protected def isPut: Boolean = {
    overriddenMethod == "PUT"
  }

  override protected def isDelete: Boolean = {
    overriddenMethod == "DELETE"
  }
}
