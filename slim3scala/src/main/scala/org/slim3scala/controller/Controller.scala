package org.slim3scala.controller

import java.lang.{ Boolean => JBoolean }
import java.lang.{ Double => JDouble }
import java.lang.{ Float => JFloat }
import java.lang.{ Integer => JInteger }
import java.lang.{ Long => JLong }
import java.lang.{ Short => JShort }
import java.util.Date
import scala.collection.JavaConversions._

import com.google.appengine.api.datastore.Key

import org.slim3.controller.{ Controller => JController }
import org.slim3.controller.Navigation

import org.slim3scala.Constants

abstract class Controller extends JController {
  protected[this] var layout: String = null

  override def runBare(): Navigation = {
    clearRequestAttributesForSlim3Scala()
    super.runBare()
  }

  protected[this] def clearRequestAttributesForSlim3Scala() {
    for {
      _name <- request.getAttributeNames
      name = _name.asInstanceOf[String]
    }
      if (name.startsWith(Constants.SYSTEM_KEY_PREFIX))
        request.removeAttribute(name)
  }

  override protected def forward(path: String): Navigation = {
    setLayoutToRequestScope()
    super.forward(path)
  }

  protected[this] def setLayoutToRequestScope() {
    if (isDevelopment && requestScope(Constants.LAYOUT_KEY) != null) {
      import java.util.logging.Level
      import java.util.logging.Logger
      val logger = Logger.getLogger(getClass.getName)
      if (logger.isLoggable(Level.WARNING)) {
        logger.log(Level.WARNING,
          """requestScope("%s") is overwritten by layout"""
            .format(Constants.LAYOUT_KEY))
      }
    }
    if (layout == null) {
      removeRequestScope(Constants.LAYOUT_KEY)
    } else {
      requestScope(Constants.LAYOUT_KEY, layout)
    }
  }

  // override as public for view
  override def param(name: CharSequence): String                        = super.param(name)
  override def paramValues(name: CharSequence): Array[String]           = super.paramValues(name)
  override def asString(name: CharSequence): String                     = super.asString(name)
  override def asShort(name: CharSequence): JShort                      = super.asShort(name)
  override def asShort(name: CharSequence, pattern: String): JShort     = super.asShort(name, pattern)
  override def asInteger(name: CharSequence): JInteger                  = super.asInteger(name)
  override def asInteger(name: CharSequence, pattern: String): JInteger = super.asInteger(name, pattern)
  override def asLong(name: CharSequence): JLong                        = super.asLong(name)
  override def asLong(name: CharSequence, pattern: String): JLong       = super.asLong(name, pattern)
  override def asFloat(name: CharSequence): JFloat                      = super.asFloat(name)
  override def asFloat(name: CharSequence, pattern: String): JFloat     = super.asFloat(name, pattern)
  override def asDouble(name: CharSequence): JDouble                    = super.asDouble(name)
  override def asDouble(name: CharSequence, pattern: String): JDouble   = super.asDouble(name, pattern)
  override def asBoolean(name: CharSequence): JBoolean                  = super.asBoolean(name)
  override def asDate(name: CharSequence, pattern: String): Date        = super.asDate(name, pattern)
  override def asKey(name: CharSequence): Key                           = super.asKey(name)
  override def requestScope[T](name: CharSequence): T                   = super.requestScope(name)
  override def requestScope(name: CharSequence, value: Any): Unit       = super.requestScope(name, value)
  override def removeRequestScope[T](name: CharSequence): T             = super.removeRequestScope(name)
  override def sessionScope[T](name: CharSequence): T                   = super.sessionScope(name)
  override def sessionScope(name: CharSequence, value: Any): Unit       = super.sessionScope(name, value)
  override def removeSessionScope[T](name: CharSequence): T             = super.removeSessionScope(name)
  override def applicationScope[T](name: CharSequence): T               = super.applicationScope(name)
  override def applicationScope(name: CharSequence, value: Any): Unit   = super.applicationScope(name, value)
  override def removeApplicationScope[T](name: CharSequence): T         = super.removeApplicationScope(name)
}
