package se.gigurra.wallace.client.stage.world

import se.gigurra.wallace.audio.AudioManager
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.gamemodel.WorldEvent

case class AudioStateManager(statCfg: StaticConfiguration,
                             dynCfg: DynamicConfiguration) {

  private val audioManager = AudioManager(Seq("audio"))

  def volume = dynCfg.audio_volume

  def update(worldEvents: Seq[WorldEvent]): Unit = {
    audioManager.update()
  }

}
