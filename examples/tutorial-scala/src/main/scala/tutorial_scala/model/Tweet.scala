package tutorial_scala.model

import java.util.Date
import scala.reflect.BeanProperty

import com.google.appengine.api.datastore.Key

import org.slim3.datastore.Attribute
import org.slim3.datastore.Model

@SerialVersionUID(1L)
@Model(schemaVersion = 1)
class Tweet {
  @Attribute(primaryKey = true)
  var key: Key = _

  @Attribute(version = true)
  var version: Long = _

  @BeanProperty
  var content: String = _

  var createdDate: Date = new Date

  override def hashCode: Int = {
    41 + (if (key == null) 0 else key.hashCode)
  }

  override def equals(other: Any): Boolean = other match {
    case that: Tweet =>
      (that canEqual this) &&
      key == that.key
    case _ => false
  }

  def canEqual(other: Any): Boolean = {
    other.isInstanceOf[Tweet]
  }
}
