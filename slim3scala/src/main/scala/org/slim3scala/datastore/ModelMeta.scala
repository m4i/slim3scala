package org.slim3scala.datastore

import java.util.{ List => JList }

import org.slim3.datastore.{ ModelMeta => JModelMeta }

abstract class ModelMeta[M](
    kind: String,
    modelClass: Class[M],
    classHierarchyList: JList[String])
  extends JModelMeta[M](kind, modelClass, classHierarchyList) {

  def this(kind: String, modelClass: Class[M]) =
    this(kind, modelClass, null)
}
