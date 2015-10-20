package se.gigurra.wallace.comm.kryoimpl

import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Logger

import com.esotericsoftware.kryo.Serializer
import com.esotericsoftware.kryonet.Connection
import rx.lang.scala.{Observable, Subject}
import rx.lang.scala.subjects.PublishSubject
import se.gigurra.wallace.comm.{Subscribe, Post, Topic, TopicClient}

import scala.collection.JavaConversions._

import scala.reflect.ClassTag

class KryoTopicClient[MessageType: ClassTag, SerializerType <: Serializer[_]](
  url: String,
  port: Int,
  serializerFactory: => SerializerType,
  connectTimeout: Int = 5000)
  extends KryoClient[KryoTopicClient[MessageType, SerializerType]](url, port, serializerFactory, connectTimeout)
  with TopicClient[MessageType] {

  private val subscribedSubjects = new ConcurrentHashMap[String, Subject[MessageType]]()
  private var disconnected = false

  override def received(connection: Connection, message: scala.Any): Unit = {
    message match {
      case Post(topic, message: MessageType) =>
        println(s"Client got $message")
        Option(subscribedSubjects.get(topic)).foreach(subject => {
          println("Notifying subject")
          subject.onNext(message)
        })
      case Post(topic, message) => Logger.getLogger(getClass.getName).warning(s"Unexpected message of type ${message.getClass} received")
      case null => Logger.getLogger(getClass.getName).warning(s"Unexpected null message received")
      case _ => Logger.getLogger(getClass.getName).warning(s"Unexpected message of type ${message.getClass} received")
    }
  }


  override def subscribe(topicName: String): Topic[MessageType] = {

    if (subscribedSubjects.containsKey(topicName))
      throw new RuntimeException(s"$this is already subscribed to topic '$topicName'")

    val subject = Subject[MessageType]()
    subscribedSubjects.put(topicName, subject)
    sendTcp(Subscribe(topicName))

    val topic: Topic[MessageType] = new Topic[MessageType] {

      override def name: String = topicName

      override def stream: Observable[MessageType] = subject

      override def unsubscribe(): Unit = KryoTopicClient.this.unsubscribe(topicName)
    }

    // We were disconnected during the function call
    // Ok in java >= 5 since volatile = memory barrier
    if (disconnected)
      unsubscribe(topicName)

    topic
  }

  override def close(): Unit = {
    super.close()
    disconnected(null)
  }

  override def unsubscribe(topic: String): Unit = {
    println("WHAAAAAAAAT")
    subscribedSubjects.remove(topic).onCompleted()
  }

  override def post(topic: String, message: MessageType) = {
    sendTcp(Post(topic, message))
  }

  override def disconnected(c: Connection): Unit = {
    println(s"$this disconnected")
    disconnected = true
    subscribedSubjects.keys.foreach(unsubscribe)
  }
}
