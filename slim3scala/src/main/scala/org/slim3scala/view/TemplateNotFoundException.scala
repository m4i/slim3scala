package org.slim3scala.view

class TemplateNotFoundException(val path: String)
  extends RuntimeException("Could not reload template - " + path)
