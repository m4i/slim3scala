package tutorial_scala.controller.twitter

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

import org.slim3.datastore.Datastore
import org.slim3scala.tester.ControllerSuite

import tutorial_scala.model.Tweet

class TweetControllerSuite
  extends FunSuite with ControllerSuite with ShouldMatchers {

  test("run") {
    tester.param("content", "Hello")
    tester.start("/twitter/tweet")
    val controller = tester.getController[TweetController]
    controller should not equal (null)
    tester.isRedirect should equal (true)
    tester.getDestinationPath should equal ("/twitter/")
    val stored = Datastore.query(classOf[Tweet]).asSingle
    stored should not equal (null)
    stored.content should equal ("Hello")
  }
}
