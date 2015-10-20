package se.gigurra.wallace.comm.kryoimpl

import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Logger

import com.esotericsoftware.kryo.Serializer
import com.esotericsoftware.kryonet.{Connection, FrameworkMessage}
import se.gigurra.wallace.comm._

import scala.collection.JavaConversions._
import scala.concurrent.duration.Duration
import scala.reflect.ClassTag

class KryoTopicClient[MessageType: ClassTag, SerializerType <: Serializer[_]](
  url: String,
  port: Int,
  serializerFactory: => SerializerType,
  connectTimeout: Int = 5000)
  extends KryoClient[KryoTopicClient[MessageType, SerializerType]](url, port, serializerFactory, connectTimeout)
  with TopicClient[MessageType] {

  private val topics = new ConcurrentHashMap[String, Topic[MessageType]]()
  private var disconnected = false

  override def received(connection: Connection, message: scala.Any): Unit = {
    message match {
      case Post(topic, message: MessageType) => Option(topics.get(topic)).foreach(_.publish(message))
      case Post(topic, message) => Logger.getLogger(getClass.getName).warning(s"Unexpected message of type ${message.getClass} received")
      case _: FrameworkMessage =>
      case null => Logger.getLogger(getClass.getName).warning(s"Unexpected null message received on client")
      case _ => Logger.getLogger(getClass.getName).warning(s"Unexpected message of type ${message.getClass} received on client")
    }
  }

  override def subscribe(
    topicName: String,
    historySize: Int,
    historyTimeout: Duration): Subscription[MessageType] = {

    if (topics.containsKey(topicName))
      throw new RuntimeException(s"$this is already subscribed to topic '$topicName'")

    val topic = new Topic[MessageType](topicName, historySize, historyTimeout)
    topics.put(topicName, topic)
    sendTcp(Subscribe(topic.name))

    // We were disconnected during the function call
    // Ok in java >= 5 since volatile = memory barrier
    if (disconnected)
      unsubscribe(topicName)

    Subscription[MessageType](topicName, topic.masterSource, () => unsubscribe(topicName))
  }

  override def close(): Unit = {
    topics.keys.foreach(unsubscribe)
    super.close()
    disconnected(null)
  }

  override def unsubscribe(topicName: String): Unit = {
    Option(topics.remove(topicName)).foreach{ topic =>
      sendTcp(Unsubscribe(topicName))
    }
  }

  override def post(topic: String, message: MessageType) = {
    sendTcp(Post(topic, message))
  }

  override def disconnected(c: Connection): Unit = {
    disconnected = true
    topics.keys.foreach(unsubscribe)
  }
}
