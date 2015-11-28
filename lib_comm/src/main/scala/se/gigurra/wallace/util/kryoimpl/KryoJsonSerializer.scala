package se.gigurra.wallace.util.kryoimpl

import java.io.{InputStreamReader, OutputStreamWriter}
import java.nio.charset.Charset

import com.esotericsoftware.kryo
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{Input, Output}
import org.json4s._
import org.json4s.native.Serialization.{read => readJson, write => writeJson}

class KryoJsonSerializer extends kryo.Serializer[Object] {

  val utf8 = Charset.forName("UTF-8")


  override def write(kryo: Kryo, fos: Output, _o: Object): Unit = {
    val writer = new OutputStreamWriter(fos, utf8)
    writeJson(_o, writer)(DefaultFormats)
    writer.flush()
  }

  override def read(kryo: Kryo, fis: Input, typ: Class[Object]): Object = {
    val reader = new InputStreamReader(fis, utf8)
    val manifest = Manifest.classType(typ)
    readJson(reader)(DefaultFormats, manifest)
  }

}
