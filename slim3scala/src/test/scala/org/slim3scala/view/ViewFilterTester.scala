package org.slim3scala.view

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers

import org.slim3.controller.ControllerConstants
import org.slim3.tester.MockFilterChain
import org.slim3.tester.MockFilterConfig
import org.slim3.tester.MockHttpServletRequest
import org.slim3.tester.MockHttpServletResponse
import org.slim3.tester.MockServletContext

trait ViewFilterTester extends WordSpec with ShouldMatchers {
  val rootPackageName = "foo.bar"

  val servletContext = new MockServletContext
  servletContext.setInitParameter(
    ControllerConstants.ROOT_PACKAGE_KEY,
    rootPackageName)

  val filterConfig = new MockFilterConfig(servletContext)
  val filterChain  = new MockFilterChain

  def createRequest()  = new MockHttpServletRequest(servletContext)
  def createResponse() = new MockHttpServletResponse
}
