package tutorial_scala.controller.twitter

import org.slim3.controller.Navigation
import org.slim3scala.controller.Controller

import tutorial_scala.service.TwitterService

class IndexController extends Controller {
  private val service = new TwitterService

  lazy val tweetList = service.getTweetList

  protected def run(): Navigation = {
    forward("index.html")
  }
}
