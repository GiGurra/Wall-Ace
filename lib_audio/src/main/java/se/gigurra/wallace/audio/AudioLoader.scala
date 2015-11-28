package se.gigurra.wallace.audio

import se.gigurra.wallace.util.Time

case class AudioLoader(resourcePaths: Seq[String]) {
  import AudioLoader._

  def loadOneOff: Playable = ???

}

object AudioLoader {

  trait Playable {
    def play(): Unit
    def stop(): Unit
    def setVolume(v: Float)
    def length: Time.Seconds
  }

}
