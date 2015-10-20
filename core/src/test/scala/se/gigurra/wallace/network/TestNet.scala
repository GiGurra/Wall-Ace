package se.gigurra.wallace.network

import com.esotericsoftware.kryonet.Connection
import org.scalatest._

case class Post(topic: String, content: String)

case class Subscribe(topic: String)

case class Unsubscribe(topic: String)

class TestNet extends FlatSpec with Matchers {

  "TestNet" should "send some binary messages" in {

    val server = new TopicServer(123) {
      override def received(connection: Connection, message: Object): Unit = {
        println(s"Got message: $message")
      }
    }.start()
    val client = new TopicClient("127.0.0.1", 123).start()

    for (endpt <- Seq(server, client)) {
      endpt.register[Array[Byte]]
      endpt.register[String]
      endpt.register[Post]
      endpt.register[Subscribe]
      endpt.register[Unsubscribe]
    }

    client.sendTcp("Hello".getBytes)
    client.sendTcp(Post(topic = "some.topic", content = "Hello"))
    client.sendTcp(Post(topic = "some.other.topic", content = "World!"))
    client.sendTcp(Subscribe(topic = "some.topic"))
    client.sendTcp(Subscribe(topic = "some.other.topic"))
    client.sendTcp(Unsubscribe(topic = "some.topic"))

    Thread.sleep(1000)
  }

  "TestNet" should "send some json messages" in {

    val server = new TopicServer(123, new DefaultJsonSerializer) {
      override def received(connection: Connection, message: Object): Unit = {
        println(s"Got message: $message")
      }
    }.start()
    val client = new TopicClient("127.0.0.1", 123, new DefaultJsonSerializer).start()

    for (endpt <- Seq(server, client)) {
      endpt.register[Array[Byte]]
      endpt.register[String]
      endpt.register[Post]
      endpt.register[Subscribe]
      endpt.register[Unsubscribe]
    }

    client.sendTcp("Hello".getBytes)
    client.sendTcp(Post(topic = "some.topic", content = "Hello"))
    client.sendTcp(Post(topic = "some.other.topic", content = "World!"))
    client.sendTcp(Subscribe(topic = "some.topic"))
    client.sendTcp(Subscribe(topic = "some.other.topic"))
    client.sendTcp(Unsubscribe(topic = "some.topic"))

    Thread.sleep(1000)
  }

}