package se.gigurra.wallace.client.stage.world.audio

import java.io.Closeable

import se.gigurra.wallace.audio.AudioManager
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.gamemodel.{WorldSimFrameIndex, WorldEvent}

case class AudioStateManager(statCfg: StaticConfiguration,
                             dynCfg: DynamicConfiguration) extends Closeable {

  private val audioManager = AudioManager(Seq("audio"))

  def volume = dynCfg.audio_volume

  def update(iSimFrame: WorldSimFrameIndex, events: Seq[WorldEvent]): Unit = {
    audioManager.update()
  }

  def close(): Unit = {
    audioManager.close()
  }

}
