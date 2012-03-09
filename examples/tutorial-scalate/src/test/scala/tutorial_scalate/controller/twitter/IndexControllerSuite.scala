package tutorial_scalate.controller.twitter

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

import org.slim3scala.tester.ControllerSuite

class IndexControllerSuite
  extends FunSuite with ControllerSuite with ShouldMatchers {

  test("run") {
    tester.start("/twitter/")
    val controller = tester.getController[IndexController]
    controller should not equal (null)
    tester.isRedirect should equal (false)
    tester.getDestinationPath should equal ("/twitter/index.html.scaml")
    controller.tweetList should not equal (null)
  }
}
