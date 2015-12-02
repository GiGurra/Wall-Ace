package se.gigurra.wallace.audio

import se.gigurra.wallace.util.Time

import scala.collection.mutable.ArrayBuffer


case class AudioManager(loader: AudioLoader) {

  private val playingSounds = new ArrayBuffer[Sound]

  def update(): Unit = {
    implicit val time = Time.seconds
    val expired = playingSounds.filter(_.expired)
    playingSounds --= expired
    expired.foreach(_.stopNow())
  }

  def close(): Unit = {
    playingSounds.foreach(_.stopNow())
    playingSounds.clear()
  }

  def playOnce(id: String,
               volume: Float = 1.0f): Sound = ???

  def loop(id: String,
           n: Int = Int.MaxValue,
           volume: Float = 1.0f): Sound = ???

}

object AudioManager {

  def apply(searchPaths: Seq[String]): AudioManager = {
    AudioManager(AudioLoader(searchPaths))
  }
}
