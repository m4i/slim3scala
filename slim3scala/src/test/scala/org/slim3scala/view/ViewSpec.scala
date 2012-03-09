package org.slim3scala.view

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers

import org.slim3.tester.MockServletContext
import org.slim3.tester.MockHttpServletRequest
import org.slim3.tester.MockHttpServletResponse

class ViewSpec extends WordSpec with ShouldMatchers {
  val rootPackageName = "foo.bar"

  val servletContext = new MockServletContext

  def createRequest()  = new MockHttpServletRequest(servletContext)
  def createResponse() = new MockHttpServletResponse
  def createView()     = new View(
    servletContext, createRequest(), createResponse(), null, rootPackageName)

  "A View" should {
    "throw TemplateNotFoundException " +
        "if a view render the nonexistent path" in {
      evaluating {
        createView().render("/xxx")
      } should produce [TemplateNotFoundException]
    }
  }
}
