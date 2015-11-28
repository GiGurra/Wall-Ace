package se.gigurra.wallace.util.kryoimpl

import java.util.concurrent.TimeUnit
import java.util.logging.Logger

import com.esotericsoftware.kryo.Serializer
import com.esotericsoftware.kryonet.{Connection, FrameworkMessage, Listener}
import se.gigurra.wallace.util._

import scala.concurrent.duration.Duration
import scala.language.implicitConversions
import scala.reflect.ClassTag

class KryoTopicServer[SerializerType <: Serializer[_]: ClassTag](
  val port: Int,
  serializerFactory: => SerializerType,
  val historySize: Int = 128,
  val historyTimeout: Duration = Duration(1, TimeUnit.HOURS))
  extends Listener
  with SerialRegisterable {

  private val topics = new TopicManager(new Topic[Any](_, historySize, historyTimeout))

  override def connected(connection: Connection): Unit = {
  }

  override def disconnected(connection: Connection): Unit = {
  }

  override def received(connection: Connection, message: Object): Unit = {
    message match {
      case Post(topic, message) => topics.post(topic, message)
      case Subscribe(topic) => topics.subscribe(connection, topic)
      case Unsubscribe(topic) => topics.unsubscribe(connection, topic)
      case _: FrameworkMessage =>
      case null => Logger.getLogger(getClass.getName).warning(s"Unexpected null message received on client")
      case _ => Logger.getLogger(getClass.getName).warning(s"Unexpected message of type ${message.getClass} received on client")
    }
  }

  val server = new KryoServer(port, serializerFactory)

  def register[T: ClassTag](): Unit = {
    server.register[T]()
  }

  def start() = {
    server.start(this)
    this
  }

  def close(): Unit = {
    server.close()
  }

  implicit def toTopicClient(connection: Connection): TopicManager.Client[Any] = RichConnection(connection)

  case class RichConnection(val connection: Connection) extends TopicManager.Client[Any] {
    def post(topic: String, message: Any) = {
      connection.sendTCP(Post(topic, message))
    }

    override def subscribed(topic: String): Unit = {
      connection.sendTCP(Subscribed(topic))
    }

    override def unsubscribed(topic: String): Unit =  {
      connection.sendTCP(Unsubscribed(topic))
    }
  }

}
