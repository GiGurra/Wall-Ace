package se.gigurra.wallace.comm

import scala.collection.mutable

object TopicManager {
  trait Client[MessageType] {
    def post(topic: String, message: MessageType)
  }
}

class TopicManager[MessageType](topicFactory: String => Topic[MessageType]) {
  import TopicManager._

  private val topics = new mutable.HashMap[String, Topic[MessageType]]()
  private val subscriptions = new mutable.HashMap[(Client[MessageType], String), Subscription[MessageType]]()

  def subscribe(client: Client[MessageType], topicName: String): Unit = {

    if (subscriptions.contains((client, topicName)))
      throw new RuntimeException(s"Client $client already subscribes to $topicName")

    val subscription = getOrCreateTopic(topicName).subscribe()
    subscriptions.put((client, topicName), subscription)

    subscription.stream.foreach(client.post(topicName, _))
  }

  def unsubscribe(client: Client[MessageType], topicName: String): Unit = {
    subscriptions.remove((client, topicName)) match {
      case Some(subscription) => subscription.unsubscribe()
      case None => throw new RuntimeException(s"Client $client does not subscribe to $topicName")
    }
  }

  def post(topicName: String, message: MessageType): Unit = {
    getOrCreateTopic(topicName).publish(message)
  }

  private[this] def getOrCreateTopic(name: String): Topic[MessageType] = {
    topics.getOrElseUpdate(name, topicFactory(name))
  }

}
