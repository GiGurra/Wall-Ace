package se.gigurra.wallace

import se.gigurra.wallace.gamestate.World
import se.gigurra.wallace.playerinput.PlayerInputMessage
import se.gigurra.wallace.gamestate.AuthorizedStateMessage

trait IGameUpdater {

  def update(
    localPlayerCurrentInput: PlayerInputMessage,
    authorativeSnapshot: Option[AuthorizedStateMessage],
    remotePlayersNewInputs: Seq[PlayerInputMessage], // That have arrived AFTER the authorativeSnapshot
    localIterationIndex: Long,
    localTime: Double,
    lastLocalTime: Double)

}