package se.gigurra.wallace

import se.gigurra.wallace.gamestate.ServerStepMessage
import se.gigurra.wallace.playerinput.PlayerInputMessage

trait IGameloopInput {

  def getLocalPlayerCurrentInput(): PlayerInputMessage

  def getNewInputsFromServer(): Seq[ServerStepMessage]

}