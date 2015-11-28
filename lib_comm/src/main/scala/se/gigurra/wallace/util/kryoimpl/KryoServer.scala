package se.gigurra.wallace.util.kryoimpl

import com.esotericsoftware.kryo.factories.SerializerFactory
import com.esotericsoftware.kryo.{Kryo, Serializer}
import com.esotericsoftware.kryonet.Listener

import scala.reflect.ClassTag

class KryoServer(val port: Int,
                 serializerFactory: => Serializer[_] = new KryoBinarySerializer)
  extends SerialRegisterable {

  private val server = new com.esotericsoftware.kryonet.Server()
  server.getKryo.setDefaultSerializer(new SerializerFactory {
    override def makeSerializer(kryo: Kryo, `type`: Class[_]) = serializerFactory
  })

  def start(listener: Listener): Unit = {
    server.addListener(listener)
    server.start()
    server.bind(port) // TCP only - Linux has issues with > 25 udp connections
  }

  def close(): Unit = {
    server.stop()
  }

  def register[T: ClassTag](): Unit = {
    server.getKryo.register(scala.reflect.classTag[T].runtimeClass)
  }

}