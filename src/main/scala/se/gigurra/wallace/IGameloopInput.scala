package se.gigurra.wallace

import se.gigurra.wallace.gamestate.AuthorizedStateMessage
import se.gigurra.wallace.playerinput.PlayerInputMessage
import se.gigurra.wallace.gamestate.World

trait IGameloopInput {

  def getLocalPlayerCurrentInput(): PlayerInputMessage

  def getAuthorativeSnapshot(): Option[AuthorizedStateMessage]

  def getRemotePlayersNewInputs(): Seq[PlayerInputMessage]

}