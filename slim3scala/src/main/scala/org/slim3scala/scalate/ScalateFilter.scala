package org.slim3scala.scalate

import org.fusesource.scalate.TemplateEngine
import org.fusesource.scalate.RenderContext

import org.slim3.util.RequestUtil

import org.slim3scala.view.ViewFilter

class ScalateFilter extends ViewFilter {
  protected lazy val scalateEngine = createScalateEngine()

  override protected def getContentExtension(path: String): String = {
    if (isScalate(path)) {
      RequestUtil.getExtension(path.take(path.lastIndexOf('.')))
    } else {
      super.getContentExtension(path)
    }
  }

  override protected def render(path: String) {
    if (isScalate(path)) {
      render(createScalateContext(), path)
    } else {
      super.render(path)
    }
  }

  protected def render(context: RenderContext, path: String) {
    scalateEngine.classLoader = Thread.currentThread.getContextClassLoader
    context.include(path, true)
  }

  protected def createScalateEngine(): TemplateEngine = {
    val engine = new TemplateEngine
    engine.allowCaching   = false
    engine.allowReload    = false
    engine.layoutStrategy = new SimpleLayoutStrategy
    engine.resourceLoader = new NullResourceLoader
    engine
  }

  protected def createScalateContext(): RenderContext = {
    val context = new PackagedRenderContext(scalateEngine,
      request.value, response.value, servletContext, rootPackageName)
    context.attributes("controller") = getController
    context
  }

  protected def isScalate(path: String): Boolean = {
    TemplateEngine.templateTypes.contains(RequestUtil.getExtension(path))
  }
}
