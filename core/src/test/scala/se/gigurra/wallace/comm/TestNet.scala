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

      val fixture = makeFixture[Any, KryoBinarySerializer](1024 + 0)()
      import fixture._

      client.sendTcp("Hello".getBytes)
      client.sendTcp(Post(topic = "some.topic", content = "Hello"))
      client.sendTcp(Post(topic = "some.other.topic", content = "World!"))
      client.sendTcp(Subscribe(topic = "some.topic"))
      client.sendTcp(Subscribe(topic = "some.other.topic"))
      client.sendTcp(Unsubscribe(topic = "some.topic"))

      fixture.close()
      Thread.sleep(100)
    }

    "send some json messages" in {

      val fixture = makeFixture[Any, KryoJsonSerializer](1024 + 1)()
      import fixture._

      client.sendTcp("Hello".getBytes)
      client.sendTcp(Post(topic = "some.topic", content = "Hello"))
      client.sendTcp(Post(topic = "some.other.topic", content = "World!"))
      client.sendTcp(Subscribe(topic = "some.topic"))
      client.sendTcp(Subscribe(topic = "some.other.topic"))
      client.sendTcp(Unsubscribe(topic = "some.topic"))

      fixture.close()
      Thread.sleep(100)
    }

    "subscribe to some topic and receive messages in it, even when it starts listening late" in {

      val fixture = makeTopicFixture[String, KryoJsonSerializer](1024 + 2){
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

      val fixture = makeTopicFixture[String, KryoJsonSerializer](1024 + 3){
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

      val fixture = makeTopicFixture[String, KryoJsonSerializer](1024 + 4){
        case (connection, message: Subscribe) => connection.sendTCP(Post(message.topic, "Hello and welcome to the server"))
        case (connection, message) => println(s"Client got unknown $message")
      }
      import fixture._

      val subscription = client.subscribe("My Topic", historyTimeout = Duration(10, TimeUnit.SECONDS))
      val stream = subscription.stream
      Thread.sleep(200) // Start listening late
      assert(finishes(stream.toBlocking.head))
      fixture.close()
      Thread.sleep(100)
    }

    "drop messages in subscribed topics if they exceed the buffer size when noone is listening" in {

      val fixture = makeTopicFixture[String, KryoJsonSerializer](1024 + 5){
        case (connection, message: Subscribe) => connection.sendTCP(Post(message.topic, "Hello and welcome to the server"))
        case (connection, message) => println(s"Client got unknown $message")
      }

      import fixture._

      val subscription = client.subscribe("My Topic", historySize = 0)
      val stream = subscription.stream
      Thread.sleep(200) // Start listening late
      assert(timesOut(stream.toBlocking.head))
      fixture.close()
      Thread.sleep(100)
    }


    "Receive messages from subscribed topics" in {

      val fixture = makeTopicFixture[String, KryoJsonSerializer](1024 + 6)()
      val client2 = makeClient[String, KryoJsonSerializer](1024 + 6)
      import fixture._

      val subscription = client.subscribe("My Topic")
      client2.post("My Topic", "Hello")
      assert(finishes(subscription.stream.toBlocking.head))

      client2.close()
      fixture.close()
      Thread.sleep(100)
    }

    "Receive binary messages from subscribed topics" in {

      val fixture = makeTopicFixture[Array[Byte], KryoBinarySerializer](1024 + 7)()
      val client2 = makeClient[Array[Byte], KryoBinarySerializer](1024 + 7)
      import fixture._

      val subscription = client.subscribe("My Topic")
      client2.post("My Topic", "Hello".getBytes())
      assert(finishesTrue(new String(subscription.stream.toBlocking.head) == "Hello"))

      client2.close()
      fixture.close()
      Thread.sleep(100)
    }

    "Ignore messages if not subscribed to topic" in {

      val fixture = makeTopicFixture[String, KryoJsonSerializer](1024 + 8)()
      val client2 = makeClient[String, KryoJsonSerializer](1024 + 8)
      import fixture._

      val subscription = client.subscribe("My Topic")
      client2.post("My Other Topic", "Hello")
      assert(timesOut(subscription.stream.toBlocking.head))

      client2.close()
      fixture.close()
      Thread.sleep(100)
    }

    "Stop receiving messages after unsubscribing from topics" in {

      val fixture = makeTopicFixture[String, KryoJsonSerializer](1024 + 9)()
      val client2 = makeClient[String, KryoJsonSerializer](1024 + 9)
      import fixture._

      val subscription = client.subscribe("My Topic")
      val items = subscription.stream.toBlocking.next

      client2.post("My Topic", "Hello1")
      assert(finishes(items.head))
      client.unsubscribe("My Topic")
      assert(finishes(subscription.awaitComplete()))

      client2.close()
      fixture.close()
      Thread.sleep(100)
    }

    "Stream is completed on disconnect" in {

      val fixture = makeTopicFixture[String, KryoJsonSerializer](1024 + 10)()
      import fixture._

      val subscription = client.subscribe("My Topic")
      val stream = subscription.stream.toBlocking

      client.post("My Topic", "Hello1")
      client.post("My Topic", "Hello1")
      client.post("My Topic", "Hello1")
      assert(finishes(stream.toIterable.take(3)))

      client.close()
      assert(finishes(subscription.awaitComplete()))

      fixture.close()
      Thread.sleep(100)
    }

    "All clients can receive from the same topic" in {

      val fixture = makeTopicFixture[String, KryoJsonSerializer](1024 + 11)()
      val extraClients = (0 until 50) map (_ => makeClient[String, KryoJsonSerializer](1024 + 11))
      import fixture._

      client.post("X", "Hello")
      val subs = extraClients.map(_.subscribe("X"))

      assert(finishesTrue(subs.forall(s => s.stream.toBlocking.head == "Hello")))

      val subscription = client.subscribe("My Topic")
      val stream = subscription.stream
      assert(finishes(stream.toBlocking.next))

      extraClients.foreach(_.close())
      fixture.close()
      Thread.sleep(300)
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