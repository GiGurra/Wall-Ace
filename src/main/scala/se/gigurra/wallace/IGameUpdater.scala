package se.gigurra.wallace

import se.gigurra.wallace.gamestate.World
import se.gigurra.wallace.playerinput.PlayerInputMessage
import se.gigurra.wallace.gamestate.ServerStepMessage
import se.gigurra.wallace.playerinput.Input

trait IGameUpdater {

  def update(serverStepMessage: ServerStepMessage)
    
  def predict(ownInput: Seq[Input])

}