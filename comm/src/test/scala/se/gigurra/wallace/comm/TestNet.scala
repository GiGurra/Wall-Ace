package se.gigurra.wallace.comm

import java.util.concurrent.TimeUnit

import com.esotericsoftware.kryo.Serializer
import com.esotericsoftware.kryonet.Connection
import org.scalatest._
import resource._
import se.gigurra.wallace.comm.kryoimpl._
import se.gigurra.wallace.util.Decorated

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, TimeoutException}
import scala.reflect.ClassTag
import scala.util.Try
import scala.language.implicitConversions

class TestNet extends WordSpec with Matchers {

  "TestNet" should {

    "send some binary messages" in {

      for (fixture <- managed(makeFixture[Any, KryoBinarySerializer](1024 + 0)())) {
        import fixture._
        client.sendTcp("Hello".getBytes)
        client.sendTcp(Post(topic = "some.topic", content = "Hello"))
        client.sendTcp(Post(topic = "some.other.topic", content = "World!"))
        client.sendTcp(Subscribe(topic = "some.topic"))
        client.sendTcp(Subscribe(topic = "some.other.topic"))
        client.sendTcp(Unsubscribe(topic = "some.topic"))
      }
    }

    "send some json messages" in {

      for (fixture <- managed(makeFixture[Any, KryoJsonSerializer](1024 + 1)())) {
        import fixture._
        client.sendTcp("Hello".getBytes)
        client.sendTcp(Post(topic = "some.topic", content = "Hello"))
        client.sendTcp(Post(topic = "some.other.topic", content = "World!"))
        client.sendTcp(Subscribe(topic = "some.topic"))
        client.sendTcp(Subscribe(topic = "some.other.topic"))
        client.sendTcp(Unsubscribe(topic = "some.topic"))
      }
    }

    "subscribe to some topic and receive messages in it, even when it starts listening late" in {

      for (fixture <- managed(makeTopicFixture[String, KryoJsonSerializer](1024 + 2){
        case (connection, message: Subscribe) => connection.sendTCP(Post(message.topic, "Hello and welcome to the server"))
        case (connection, message) => println(s"Client got unknown $message")
      })) {
        import fixture._
        val subscription = client.subscribe("My Topic")
        Thread.sleep(1000) // Start listening late
        assert(subscription.stream.toBlocking.head == "Hello and welcome to the server")
      }
    }

    "drop messages in subscribed topics if they're not collected in time" in {

      for (fixture <- managed(makeTopicFixture[String, KryoJsonSerializer](1024 + 3){
        case (connection, message: Subscribe) => connection.sendTCP(Post(message.topic, "Hello and welcome to the server"))
        case (connection, message) => println(s"Client got unknown $message")
      })) {
        import fixture._
        val subscription = client.subscribe("My Topic", historyTimeout = Duration(1, TimeUnit.MILLISECONDS))
        Thread.sleep(1000) // Start listening late
        assert(timesOut(subscription.stream.toBlocking.head))
      }
    }

    "not drop messages in subscribed topics if they're collected in time" in {

      for (fixture <- managed(makeTopicFixture[String, KryoJsonSerializer](1024 + 4){
        case (connection, message: Subscribe) => connection.sendTCP(Post(message.topic, "Hello and welcome to the server"))
        case (connection, message) => println(s"Client got unknown $message")
      })) {
        import fixture._
        val subscription = client.subscribe("My Topic", historyTimeout = Duration(10, TimeUnit.SECONDS))
        Thread.sleep(200) // Start listening late
        assert(finishes(subscription.stream.toBlocking.head))
      }
    }

    "drop messages in subscribed topics if they exceed the buffer size when noone is listening" in {
      for (fixture <- managed(makeTopicFixture[String, KryoJsonSerializer](1024 + 5){
        case (connection, message: Subscribe) => connection.sendTCP(Post(message.topic, "Hello and welcome to the server"))
        case (connection, message) => println(s"Client got unknown $message")
      })) {
        import fixture._
        val subscription = client.subscribe("My Topic", historySize = 0)
        val stream = subscription.stream
        Thread.sleep(200) // Start listening late
        assert(timesOut(stream.toBlocking.head))
      }
    }


    "Receive messages from subscribed topics" in {

      for {
        fixture <- managed(makeTopicFixture[String, KryoJsonSerializer](1024 + 6)())
        client2 <- managed(makeClient[String, KryoJsonSerializer](1024 + 6))
      } {
        import fixture._
        val subscription = client.subscribe("My Topic")
        client2.post("My Topic", "Hello")
        assert(finishes(subscription.stream.toBlocking.head))
      }
    }

    "Receive binary messages from subscribed topics" in {

      for {
        fixture <- managed(makeTopicFixture[Array[Byte], KryoBinarySerializer](1024 + 7)())
        client2 <- managed(makeClient[Array[Byte], KryoBinarySerializer](1024 + 7))
      } {
        import fixture._
        val subscription = client.subscribe("My Topic")
        client2.post("My Topic", "Hello".getBytes())
        assert(finishesTrue(new String(subscription.stream.toBlocking.head) == "Hello"))
      }
    }

    "Ignore messages if not subscribed to topic" in {

      for {
        fixture <- managed(makeTopicFixture[String, KryoJsonSerializer](1024 + 8)())
        client2 <- managed(makeClient[String, KryoJsonSerializer](1024 + 8))
      } {
        import fixture._
        val subscription = client.subscribe("My Topic")
        client2.post("My Other Topic", "Hello")
        assert(timesOut(subscription.stream.toBlocking.head))
      }
    }

    "Stop receiving messages after unsubscribing from topics" in {

      for {
        fixture <- managed(makeTopicFixture[String, KryoJsonSerializer](1024 + 9)())
        client2 <- managed(makeClient[String, KryoJsonSerializer](1024 + 9))
      } {
        import fixture._
        val subscription = client.subscribe("My Topic")
        val items = subscription.stream.toBlocking.next
        client2.post("My Topic", "Hello1")
        assert(finishes(items.head))
        client.unsubscribe("My Topic")
        assert(finishes(subscription.awaitComplete()))
      }
    }

    "Stream is completed on disconnect" in {

      for {
        fixture <- managed(makeTopicFixture[String, KryoJsonSerializer](1024 + 10)())
      } {
        import fixture._
        val subscription = client.subscribe("My Topic")
        val stream = subscription.stream.toBlocking
        client.post("My Topic", "Hello1")
        client.post("My Topic", "Hello1")
        client.post("My Topic", "Hello1")
        assert(finishes(stream.toIterable.take(3)))
        client.close()
        assert(finishes(subscription.awaitComplete()))
      }
    }

    "All clients can receive from the same topic" in {

      for {
        fixture <- managed(makeTopicFixture[String, KryoJsonSerializer](1024 + 11)())
        extraClients <- mmanaged((0 until 50) map (_ => makeClient[String, KryoJsonSerializer](1024 + 11)))
      } {
        import fixture._
        client.post("X", "Hello")
        val subs = extraClients.map(_.subscribe("X"))
        assert(finishesTrue(subs.forall(s => s.stream.toBlocking.head == "Hello")))
        val subscription = client.subscribe("My Topic")
        val stream = subscription.stream
        assert(finishes(stream.toBlocking.next))
      }
    }

  }

  implicit def mmanaged[A: Resource : Manifest](s: Iterable[A]) = managed(RichSeqResource(s))

  case class RichSeqResource[A: Resource : Manifest](
    val resources: Iterable[A]) extends Decorated[Iterable[A]](resources) {
    def close(): Unit = {
      resources.foreach(resource => Try(implicitly[Resource[A]].close(resource)))
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

  def await[AnyReturnType](op: => AnyReturnType, t: Duration = Duration(1, TimeUnit.SECONDS)): Try[AnyReturnType] = {
    Try(Await.result(Future(op)(scala.concurrent.ExecutionContext.Implicits.global), atMost = Duration(1, TimeUnit.SECONDS)))
  }

  def finishesTrue(op: => Boolean, t: Duration = Duration(1, TimeUnit.SECONDS)): Boolean = {
    val r = await(op, t)
    r.isSuccess && r.get
  }

  def finishes[AnyReturnType](op: => AnyReturnType, t: Duration = Duration(1, TimeUnit.SECONDS)): Boolean = {
    await(op, t).isSuccess
  }

  def timesOut[AnyReturnType](op: => AnyReturnType, t: Duration = Duration(1, TimeUnit.SECONDS)): Boolean = {
    val r = await(op, t)
    r.isFailure && r.failed.get.isInstanceOf[TimeoutException]
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
    endPoint.register[Subscribed]
    endPoint.register[Unsubscribe]
    endPoint.register[Unsubscribed]
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
        super.received(connection, message)
        serverRecvOverride(connection, message)
      }

      registerTypes(this)
    }.start()

    val client = makeClient[MessageType, SerializerType](port)

    Fixture(server, client)
  }

}