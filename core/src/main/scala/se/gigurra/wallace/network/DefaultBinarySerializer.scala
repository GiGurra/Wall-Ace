package se.gigurra.wallace.network

import java.io.{ObjectOutputStream, ObjectInputStream}

import com.esotericsoftware.kryo.io.{Output, Input}
import com.esotericsoftware.kryo.{Kryo, Serializer}

class DefaultBinarySerializer extends Serializer[Object] {

  class ObjectInputStreamWithCustomClassLoader(stream: Input) extends ObjectInputStream(stream) {
    override def resolveClass(desc: java.io.ObjectStreamClass): Class[_] = {
      try { Class.forName(desc.getName, false, getClass.getClassLoader) }
      catch { case ex: ClassNotFoundException => super.resolveClass(desc) }
    }
  }

  override def write(kryo: Kryo, fos: Output, _o: Object): Unit = {
    val o = _o.asInstanceOf[Object]
    val oos = new ObjectOutputStream(fos)
    oos.writeObject(o)
  }

  override def read(kryo: Kryo, fis: Input, typ: Class[Object]): Object = {
    val ois = new ObjectInputStreamWithCustomClassLoader(fis)
    ois.readObject()
  }

}
