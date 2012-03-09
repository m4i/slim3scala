package tutorial_java.service;

import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Transaction;

import org.slim3.datastore.Datastore;
import org.slim3.util.BeanUtil;

import tutorial_java.meta.TweetMeta;
import tutorial_java.model.Tweet;

public class TwitterService {

    private TweetMeta t = new TweetMeta();

    public Tweet tweet(Map<String, Object> input) {
        Tweet tweet = new Tweet();
        BeanUtil.copy(input, tweet);
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tweet);
        tx.commit();
        return tweet;
    }

    public List<Tweet> getTweetList() {
        return Datastore.query(t).sort(t.createdDate.desc).asList();
    }
}
