package org.slim3scala.scalate

import org.fusesource.scalate.util.ResourceNotFoundException

import org.slim3scala.view.ViewFilterTester

class ScalateFilterSpec extends ViewFilterTester {
  val scalateFilter = new ScalateFilter
  scalateFilter.init(filterConfig)

  "A ScalateFilter" should {
    "throw ResourceNotFoundException " +
        "if a view render the nonexistent scalate path" in {
      val request  = createRequest()
      val response = createResponse()

      val path = "/xxx.scaml"
      request.setServletPath(path)

      evaluating {
        scalateFilter.doFilter(request, response, filterChain)
      } should produce [ResourceNotFoundException]
    }
  }
}
