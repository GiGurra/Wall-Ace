package se.gigurra.wallace

import se.gigurra.wallace.gamestate.World
import se.gigurra.wallace.playerinput.PlayerInputMessage
import se.gigurra.wallace.gamestate.ServerStepMessage

trait IGameUpdater {

  def update(
    localPlayerCurrentInput: PlayerInputMessage,
    serverStepMessages: Seq[ServerStepMessage],
    localIterationIndex: Long,
    localTime: Double,
    lastLocalTime: Double)

}