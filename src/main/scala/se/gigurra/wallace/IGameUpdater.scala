package se.gigurra.wallace

import se.gigurra.wallace.gamestate.World
import se.gigurra.wallace.playerinput.PlayerInputMessage
import se.gigurra.wallace.gamestate.ServerStepMessage

trait IGameUpdater {

  def update(
    localPlayerCurrentInput: PlayerInputMessage,
    serverStepMessages: Option[ServerStepMessage],
    time: Time)

}