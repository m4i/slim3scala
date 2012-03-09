package org.slim3scala.view {

class ViewFilterSpec extends ViewFilterTester {
  val viewFilter = new ViewFilter {
    def getNegativeCache = negativeCache
  }
  viewFilter.init(filterConfig)

  "A ViewFilter" should {
    "add path to negativeCache if a view render the nonexistent path" in {
      val request  = createRequest()
      val response = createResponse()

      val path = "/xxx"
      request.setServletPath(path)

      viewFilter.getNegativeCache(path) should equal (false)
      viewFilter.doFilter(request, response, filterChain)
      viewFilter.getNegativeCache(path) should equal (true)
    }

    "throw TemplateNotFoundException " +
        "if a view render the nonexistent path as a partial template" in {
      val request  = createRequest()
      val response = createResponse()

      val path = "/index.html"
      request.setServletPath(path)

      evaluating {
        viewFilter.doFilter(request, response, filterChain)
      } should produce [TemplateNotFoundException]
    }
  }
}

}

package foo.bar.view {
  import org.slim3scala.view.Template
  import org.slim3scala.view.View
  class IndexHtmlTemplate(view: View) extends Template(view) {
    protected val path = "/index.html"
    def render() = render("xxx")
  }
}
