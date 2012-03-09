package org.slim3scala.view

import scala.collection.mutable

import javax.servlet.ServletContext
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.slim3.controller.Controller
import org.slim3.util.ClassUtil
import org.slim3.util.StringUtil
import org.slim3.util.WrapRuntimeException

import org.slim3scala.Constants

class View(
    val servletContext: ServletContext,
    val request: HttpServletRequest,
    val response: HttpServletResponse,
    val controller: Controller,
    rootPackageName: String) {

  val contentFor = mutable.Map[String, Any]()

  protected val docType = "<!DOCTYPE html>"

  /**
   * ref.
   *   http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd
   *   http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd
   *   http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd
   */
  protected val emptyElementTags = List(
    "base", "meta", "link", "hr", "br", "param", "img", "area", "input", "col",
    "basefont", "isindex",
    "frame")

  def render(path: String): String = {
    val content = renderAsAnyWithLayout(path)
    content match {
      case null => ""
      case ()   => ""
      case <html>{ _* }</html> =>
        prependDocType(toEmptyElementTag(content.toString))
      case _ =>
        content.toString
    }
  }

  def renderAsAny(path: String): Any = {
    createTemplate(path).render()
  }

  protected def renderAsAnyWithLayout(path: String): Any = {
    val template = createTemplate(path)
    val content  = template.render()
    request.getAttribute(Constants.LAYOUT_KEY) match {
      case layout: String if !View.isLayoutDisabled(layout) =>
        contentFor(Constants.LAYOUT_KEY) = content
        template.render(layout)
      case _ =>
        content
    }
  }

  protected def toTemplateClassName(path: String): String = {
    val parts = path.split('/')
    rootPackageName +
      "." +
      Constants.VIEW_PACKAGE +
      parts.init.mkString(".") +
      "." +
      parts.last.split('.').map(_.capitalize).mkString +
      Constants.TEMPLATE_SUFFIX
  }

  protected def createTemplate(path: String): Template = {
    val className = toTemplateClassName(path)
    val clazz =
      try {
        ClassUtil.forName[Class[Template]](className)
      } catch {
        case _: WrapRuntimeException =>
          throw new TemplateNotFoundException(path)
      }
    clazz.getConstructor(getClass).newInstance(this).asInstanceOf[Template]
  }

  protected def prependDocType(html: String): String = {
    if (docType == null) html
    else                 docType + "\n" + html
  }

  protected def toEmptyElementTag(html: String): String = {
    (html /: emptyElementTags)((h, t) => h.replace("></" + t + ">", " />"))
  }
}

object View {
  def isLayoutDisabled(layout: String) = {
    StringUtil.isEmpty(layout) || layout.trim.isEmpty
  }
}
