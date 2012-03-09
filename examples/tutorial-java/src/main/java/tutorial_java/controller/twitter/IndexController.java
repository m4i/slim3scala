package tutorial_java.controller.twitter;

import java.util.List;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import tutorial_java.model.Tweet;
import tutorial_java.service.TwitterService;

public class IndexController extends Controller {

    private TwitterService service = new TwitterService();

    @Override
    public Navigation run() throws Exception {
        List<Tweet> tweetList = service.getTweetList();
        requestScope("tweetList", tweetList);
        return forward("index.jsp");
    }
}
