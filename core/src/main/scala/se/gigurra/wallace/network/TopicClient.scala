package se.gigurra.wallace.network

import com.esotericsoftware.kryo.Serializer
import se.gigurra.wallace.network.kryoimpl.KryoClient

class TopicClient(url: String,
                  port: Int,
                  serializerFactory: => Serializer[_] = new DefaultBinarySerializer,
                  connectTimeout: Int = 5000) extends KryoClient[TopicClient](url, port, serializerFactory, connectTimeout) {

}
