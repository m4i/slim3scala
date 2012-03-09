package tutorial_scala.model

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

import org.slim3scala.tester.AppEngineSuite

class TweetSuite
  extends FunSuite with AppEngineSuite with ShouldMatchers {

  private var model: Tweet = _

  override def beforeEach() {
    super.beforeEach()
    model = new Tweet
  }

  test("test") {
    model should not equal (null)
  }
}
