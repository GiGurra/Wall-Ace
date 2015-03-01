package se.gigurra.wallace

import se.gigurra.wallace.gamestate.World
import se.gigurra.wallace.playerinput.PlayerInputMessage
import se.gigurra.wallace.gamestate.ServerStepMessage
import se.gigurra.wallace.playerinput.Input

trait IGameUpdater {

  // Executed on clients
  def update(serverStepMessage: ServerStepMessage)

  // Executed on clients
  def predict(ownInput: Seq[Input])

  // Executed on server
  def simulate(userInputs: Seq[Input])

}