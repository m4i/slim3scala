package tutorial_scalate.service

import java.util.List
import java.util.Map

import org.slim3.datastore.Datastore
import org.slim3.util.BeanUtil

import tutorial_scalate.meta.TweetMeta
import tutorial_scalate.model.Tweet

class TwitterService {
  private val t = new TweetMeta

  def tweet(input: Map[String, AnyRef]): Tweet = {
    val tweet = new Tweet
    BeanUtil.copy(input, tweet)
    val tx = Datastore.beginTransaction()
    Datastore.put(tweet)
    tx.commit()
    tweet
  }

  def getTweetList: List[Tweet] = {
    Datastore.query(t).sort(t.createdDate.desc).asList
  }
}
