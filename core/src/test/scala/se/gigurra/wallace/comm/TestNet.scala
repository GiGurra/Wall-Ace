package se.gigurra.wallace.comm

import java.util.concurrent.TimeUnit

import com.esotericsoftware.kryo.Serializer
import com.esotericsoftware.kryonet.Connection
import org.scalatest._
import se.gigurra.wallace.comm.kryoimpl._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, TimeoutException}
import scala.reflect.ClassTag
import scala.util.Try

class TestNet extends WordSpec with Matchers {

  "TestNet" should {

    "send some binary messages" in {

      val fixture = makeFixture[Any, KryoBinarySerializer](123)()
      import fixture._

      client.sendTcp("Hello".getBytes)
      client.sendTcp(Post(topic = "some.topic", content = "Hello"))
      client.sendTcp(Post(topic = "some.other.topic", content = "World!"))
      client.sendTcp(Subscribe(topic = "some.topic"))
      client.sendTcp(Subscribe(topic = "some.other.topic"))
      client.sendTcp(Unsubscribe(topic = "some.topic"))

      Thread.sleep(1000)
      fixture.close()
      Thread.sleep(1000)
    }

    "send some json messages" in {

      val fixture = makeFixture[Any, KryoJsonSerializer](124)()
      import fixture._

      client.sendTcp("Hello".getBytes)
      client.sendTcp(Post(topic = "some.topic", content = "Hello"))
      client.sendTcp(Post(topic = "some.other.topic", content = "World!"))
      client.sendTcp(Subscribe(topic = "some.topic"))
      client.sendTcp(Subscribe(topic = "some.other.topic"))
      client.sendTcp(Unsubscribe(topic = "some.topic"))

      Thread.sleep(1000)
      fixture.close()
      Thread.sleep(1000)
    }

    "subscribe to some topic and receive messages in it, even when it starts listening late" in {

      val fixture = makeTopicFixture[String, KryoJsonSerializer](125){
        case (connection, message: Subscribe) => connection.sendTCP(Post(message.topic, "Hello and welcome to the server"))
        case (connection, message) => println(s"Client got unknown $message")
      }
      import fixture._

      val subscription = client.subscribe("My Topic")
      val stream = subscription.stream

      Thread.sleep(1000) // Start listening late
      assert(stream.toBlocking.head == "Hello and welcome to the server")
      fixture.close()
      Thread.sleep(100)
    }

    "drop messages in subscribed topics if they're not collected in time" in {

      val fixture = makeTopicFixture[String, KryoJsonSerializer](126){
        case (connection, message: Subscribe) => connection.sendTCP(Post(message.topic, "Hello and welcome to the server"))
        case (connection, message) => println(s"Client got unknown $message")
      }
      import fixture._

      val subscription = client.subscribe("My Topic", historyTimeout = Duration(1, TimeUnit.MILLISECONDS))
      val stream = subscription.stream
      Thread.sleep(1000) // Start listening late
      assert(timesOut(stream.toBlocking.head))
      fixture.close()
      Thread.sleep(100)
    }

    "not drop messages in subscribed topics if they're collected in time" in {

      val fixture = makeTopicFixture[String, KryoJsonSerializer](126){
        case (connection, message: Subscribe) => connection.sendTCP(Post(message.topic, "Hello and welcome to the server"))
        case (connection, message) => println(s"Client got unknown $message")
      }
      import fixture._

      val subscription = client.subscribe("My Topic", historyTimeout = Duration(10, TimeUnit.SECONDS))
      val stream = subscription.stream
      Thread.sleep(1000) // Start listening late
      assert(!timesOut(stream.toBlocking.head))
      fixture.close()
      Thread.sleep(100)
    }

    "drop messages in subscribed topics if they exceed the buffer size when noone is listening" in {

      val fixture = makeTopicFixture[String, KryoJsonSerializer](126){
        case (connection, message: Subscribe) => connection.sendTCP(Post(message.topic, "Hello and welcome to the server"))
        case (connection, message) => println(s"Client got unknown $message")
      }

      import fixture._

      val subscription = client.subscribe("My Topic", historySize = 0)
      val stream = subscription.stream
      Thread.sleep(1000) // Start listening late
      assert(timesOut(stream.toBlocking.head))
      fixture.close()
      Thread.sleep(100)
    }
  }


  case class TopicFixture[MessageType: ClassTag, SerializerType <: Serializer[_]](
    server: KryoTopicServer[SerializerType],
    client: TopicClient[MessageType]) {
    def close(): Unit = {
      client.close()
      server.close()
    }
  }

  case class Fixture[MessageType: ClassTag, SerializerType <: Serializer[_]](
    server: KryoTopicServer[SerializerType],
    client: KryoTopicClient[MessageType, SerializerType]) {
    def close(): Unit = {
      client.close()
      server.close()
    }
  }

  def timesOut[AnyReturnType](op: => AnyReturnType, t: Duration = Duration(1, TimeUnit.SECONDS)): Boolean = {
    val t = Try(Await.ready(Future(op)(scala.concurrent.ExecutionContext.Implicits.global), atMost = Duration(1, TimeUnit.SECONDS)))
    t.isFailure && t.failed.get.isInstanceOf[TimeoutException]
  }

  def makeTopicFixture[MessageType: ClassTag, SerializerType <: Serializer[_] : ClassTag](
    post: Int)(
    serverRecvOverride: (Connection, Object) => Unit = (c, x) => {}): TopicFixture[MessageType, SerializerType] = {
    val fix = makeFixture[MessageType, SerializerType](post)(serverRecvOverride)
    TopicFixture(fix.server, fix.client)
  }

  def registerTypes(endPoint: SerialRegisterable): Unit = {
    endPoint.register[Array[Byte]]
    endPoint.register[String]
    endPoint.register[Post[_]]
    endPoint.register[Subscribe]
    endPoint.register[Unsubscribe]
  }

  def makeClient[MessageType: ClassTag, SerializerType <: Serializer[_] : ClassTag](port: Int) = {
    val serializerFactory = () => scala.reflect.classTag[SerializerType].runtimeClass.newInstance().asInstanceOf[SerializerType]
    val client = new KryoTopicClient[MessageType, SerializerType]("127.0.0.1", port, serializerFactory())
    registerTypes(client)
    client.start()
  }

  def makeFixture[MessageType: ClassTag, SerializerType <: Serializer[_] : ClassTag](
    port: Int)(
    serverRecvOverride: (Connection, Object) => Unit = (c, x) => {}
    ): Fixture[MessageType, SerializerType] = {

    val serializerFactory = () => scala.reflect.classTag[SerializerType].runtimeClass.newInstance().asInstanceOf[SerializerType]

    val server = new KryoTopicServer(port, serializerFactory()) {
      override def received(connection: Connection, message: Object): Unit = {
        println(s"Server $this got message: $message")
        serverRecvOverride(connection, message)
      }

      registerTypes(this)
    }.start()

    val client = makeClient[MessageType, SerializerType](port)

    Fixture(server, client)
  }

}