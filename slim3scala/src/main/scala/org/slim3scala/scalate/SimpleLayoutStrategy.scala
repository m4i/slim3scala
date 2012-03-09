package org.slim3scala.scalate

import org.fusesource.scalate.RenderContext
import org.fusesource.scalate.Template
import org.fusesource.scalate.layout.LayoutStrategy

import org.slim3scala.Constants
import org.slim3scala.view.View

class SimpleLayoutStrategy extends LayoutStrategy {
  def layout(template: Template, context: RenderContext) {
    val body = context.capture(template)
    context.attributes.get(Constants.LAYOUT_KEY) match {
      case Some(layout: String) if !View.isLayoutDisabled(layout) =>
        context.render(layout, Map("body" -> body))
      case _ =>
        context << body
    }
  }
}
