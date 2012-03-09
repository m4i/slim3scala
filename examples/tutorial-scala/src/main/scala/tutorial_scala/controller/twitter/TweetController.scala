package tutorial_scala.controller.twitter

import org.slim3.controller.Navigation
import org.slim3.util.RequestMap
import org.slim3scala.controller.Controller

import tutorial_scala.service.TwitterService

class TweetController extends Controller {
  private val service = new TwitterService

  protected def run(): Navigation = {
    service.tweet(new RequestMap(request))
    redirect(basePath)
  }
}
