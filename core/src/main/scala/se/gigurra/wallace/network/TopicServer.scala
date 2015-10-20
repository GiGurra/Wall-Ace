package se.gigurra.wallace.network

import com.esotericsoftware.kryo.Serializer
import com.esotericsoftware.kryonet.{Connection, Listener}
import se.gigurra.wallace.network.kryoimpl.{SerialRegisterable, KryoServer}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

case class ConnectionData() {
  val topics = new ArrayBuffer[String]()
}

class TopicNode(val port: Int,
                serializerFactory: => Serializer[_] = new DefaultBinarySerializer)
  extends Listener
  with SerialRegisterable {

  private val connections = new mutable.HashMap[Connection, ConnectionData]

  override def connected(connection: Connection): Unit = {
    println("Got a connection")
    connections.put(connection, ConnectionData())
  }

  override def disconnected(connection: Connection): Unit = {
    println("Lost a connection")
    connections.remove(connection)
  }

  override def received(connection: Connection, message: Object): Unit = {
    message match {
      case message: Array[Byte] => println("Got a message")
      case _ => println(s"Unknown message class ${message.getClass}")
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

  implicit def toData(connection: Connection) = connections(connection)

}
