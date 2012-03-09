package org.slim3scala.controller

import javax.servlet.http.HttpServletResponse

import org.slim3.controller.Navigation
import org.slim3.util.ResponseLocator

/**
 * @todo extends org.slim3.controller.Controller
 *       but trait cannot access java protected method isGet/isPost/isPut/isDelete
 */
trait RESTful extends MethodOverride {
  protected def run(): Navigation = {
    if (isGet)    return get()
    if (isPost)   return post()
    if (isPut)    return put()
    if (isDelete) return delete()
    methodNotAllowed()
  }

  protected[this] def get(): Navigation = {
    methodNotAllowed()
  }

  protected[this] def post(): Navigation = {
    methodNotAllowed()
  }

  protected[this] def put(): Navigation = {
    methodNotAllowed()
  }

  protected[this] def delete(): Navigation = {
    methodNotAllowed()
  }

  protected[this] def methodNotAllowed(): Navigation = {
    // trait cannot access java protected variable response
    val response = ResponseLocator.get
    response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED)
    null
  }
}
