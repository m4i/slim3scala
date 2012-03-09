package org.slim3scala.datastore

import org.slim3.datastore.{ DatastoreDelegate => JDatastoreDelegate }

class DatastoreDelegate(deadline: java.lang.Double)
  extends JDatastoreDelegate(deadline) {

  def this() = this(null)
}
