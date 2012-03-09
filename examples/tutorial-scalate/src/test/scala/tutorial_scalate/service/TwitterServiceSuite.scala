package tutorial_scalate.service

import java.util.HashMap

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

import org.slim3.datastore.Datastore
import org.slim3scala.tester.AppEngineSuite

import tutorial_scalate.model.Tweet

class TwitterServiceSuite
  extends FunSuite with AppEngineSuite with ShouldMatchers {

  private var service: TwitterService = _

  override def beforeEach() {
    super.beforeEach()
    service = new TwitterService
  }

  test("test") {
    service should not equal (null)
  }

  test("tweet") {
    val input = new HashMap[String, Object]
    input.put("content", "Hello")
    val tweeted = service.tweet(input)
    tweeted should not equal (null)
    val stored = Datastore.get(classOf[Tweet], tweeted.key)
    stored.getContent should equal ("Hello")
  }

  test("getTweetList") {
    val tweet = new Tweet
    tweet.content = "Hello"
    Datastore.put(tweet)
    val tweetList = service.getTweetList
    tweetList.size should equal (1)
    tweetList.get(0).content should equal ("Hello")
  }
}
