package se.gigurra.wallace.comm

import scala.collection.mutable

trait TopicClient[MessageType] {
  def post(topic: String, message: MessageType)
}

class TopicHost[MessageType](
  topicFactory: String => Topic[MessageType]) {

  private val topics = new mutable.HashMap[String, Topic[MessageType]]()
  private val subscriptions = new mutable.HashMap[(TopicClient[MessageType], String), Subscription[MessageType]]()
  private val subscribersPerTopic = new mutable.HashMap[String, Int]

  def subscribe(client: TopicClient[MessageType], topicName: String): Unit = {

    if (subscriptions.contains((client, topicName)))
      throw new RuntimeException(s"Client $client already subscribes to $topicName")

    val topic = topics.getOrElseUpdate(topicName, topicFactory(topicName))
    val subscription = topic.subscribe()
    subscriptions.put((client, topicName), subscription)
    subscribersPerTopic.put(topicName, subscribersPerTopic.getOrElseUpdate(topicName, 0) + 1)

    subscription.stream.foreach(client.post(topicName, _))
  }

  def unsubscribe(client: TopicClient[MessageType], topicName: String): Unit = {
    subscriptions.remove((client, topicName)) match {
      case Some(subscription) =>
        val prevCount = subscribersPerTopic.put(topicName, subscribersPerTopic(topicName) - 1).get

        if (prevCount == 1) {
          topics.remove(topicName)
          subscribersPerTopic.remove(topicName)
        }
        subscription.unsubscribe()

      case None =>
        throw new RuntimeException(s"Client $client does not subscribe to $topicName")
    }
  }

  def post(topicName: String, message: MessageType): Unit = {
    topics.getOrElseUpdate(topicName, topicFactory(topicName)).publish(message)
  }

}
