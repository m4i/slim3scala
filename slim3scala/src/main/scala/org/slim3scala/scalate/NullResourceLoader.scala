package org.slim3scala.scalate

import org.fusesource.scalate.util.Resource
import org.fusesource.scalate.util.ResourceLoader

class NullResourceLoader extends ResourceLoader {
  def resource(uri: String): Option[Resource] = None
}
