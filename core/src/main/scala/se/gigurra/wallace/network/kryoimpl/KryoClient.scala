package se.gigurra.wallace.network.kryoimpl

import com.esotericsoftware.kryo.factories.SerializerFactory
import com.esotericsoftware.kryo.{Kryo, Serializer}
import se.gigurra.wallace.network.DefaultBinarySerializer

import scala.reflect.ClassTag

abstract class KryoClient[T <: KryoClient[T]](val url: String,
                 val port: Int,
                 serializerFactory: => Serializer[_] = new DefaultBinarySerializer,
                 val connectTimeout: Int = 5000) extends SerialRegisterable {

  val kryoClient = new com.esotericsoftware.kryonet.Client()
  kryoClient.getKryo.setDefaultSerializer(new SerializerFactory {
    override def makeSerializer(kryo: Kryo, `type`: Class[_]) = serializerFactory
  })

  def start(): T = {
    kryoClient.start()
    kryoClient.connect(connectTimeout, url, port, port)
    this.asInstanceOf[T]
  }

  def close(): Unit = {
    kryoClient.stop()
  }

  def sendTcp(message: Object): Unit = {
    kryoClient.sendTCP(message)
  }

  def sendUdp(message: Object): Unit = {
    kryoClient.sendUDP(message)
  }

  def register[T: ClassTag](): Unit = {
    kryoClient.getKryo.register(scala.reflect.classTag[T].runtimeClass)
  }

}
