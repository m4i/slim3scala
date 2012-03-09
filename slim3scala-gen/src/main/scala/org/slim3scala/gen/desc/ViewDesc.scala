package org.slim3scala.gen.desc

import scala.reflect.BeanProperty

import org.slim3.gen.desc.{ ViewDesc => JViewDesc }

class ViewDesc extends JViewDesc {
  @BeanProperty
  protected var controllerClassName: String = _
}
