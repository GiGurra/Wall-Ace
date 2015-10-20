package se.gigurra.wallace.network

import java.io.InputStreamReader
import java.nio.charset.Charset

import com.esotericsoftware.kryo.io.{Input, Output}
import com.esotericsoftware.kryo.{Kryo, Serializer}
import org.json4s.{DefaultFormats, _}
import org.json4s.native.Serialization.{read => readJson, write => writeJson}

class DefaultJsonSerializer extends Serializer[Object] {

  val utf8 = Charset.forName("UTF-8")

  override def write(kryo: Kryo, fos: Output, _o: Object): Unit = {
    val str = writeJson(_o)(DefaultFormats)
    fos.writeBytes(str.getBytes(utf8))
  }

  override def read(kryo: Kryo, fis: Input, typ: Class[Object]): Object = {
    val reader = new InputStreamReader(fis, utf8)
    val manifest = Manifest.classType(typ)
    readJson(reader)(DefaultFormats, manifest)
  }

}
