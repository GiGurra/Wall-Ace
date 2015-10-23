package se.gigurra.wallace.comm.kryoimpl

import java.io.{InputStreamReader, OutputStreamWriter}
import java.nio.charset.Charset

import com.esotericsoftware.kryo
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{Input, Output}
import org.json4s._
import org.json4s.native.Serialization.{read => readJson, write => writeJson}

class KryoJsonSerializer extends kryo.Serializer[Object] {

  val utf8 = Charset.forName("UTF-8")

  class Interval(start: Long, end: Long) {
    val startTime = start
    val endTime = end
  }

  class IntervalSerializer extends CustomSerializer[Interval](format => ( {
    case JObject(JField("start", JInt(s)) :: JField("end", JInt(e)) :: Nil) =>
      new Interval(s.longValue, e.longValue)
  }, {
    case x: Interval =>
      JObject(JField("start", JInt(BigInt(x.startTime))) ::
        JField("end", JInt(BigInt(x.endTime))) :: Nil)
  }))

  val formats = org.json4s.DefaultFormats + new Serializer[Array[Byte]] {
    def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), Array[Byte]] = {
      ???
    }

    def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
      ???
    }
  }


  override def write(kryo: Kryo, fos: Output, _o: Object): Unit = {
    val writer = new OutputStreamWriter(fos, utf8)
    writeJson(_o, writer)(formats)
    writer.flush()
  }

  override def read(kryo: Kryo, fis: Input, typ: Class[Object]): Object = {
    val reader = new InputStreamReader(fis, utf8)
    val manifest = Manifest.classType(typ)
    readJson(reader)(formats, manifest)
  }

}
