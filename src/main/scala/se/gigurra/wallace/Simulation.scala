package se.gigurra.wallace

import se.gigurra.wallace.gamestate.ServerStepMessage
import se.gigurra.wallace.playerinput.PlayerInputMessage
import se.gigurra.wallace.playerinput.Input

class Simulation(gameUpdater: IGameUpdater) {

  def step(
    ownInput: Seq[Input],
    serverInput: Seq[ServerStepMessage]) {
    
    // TODO: Pre-step, whoever calls step
    // should transmit ownInput to server so it gets associated 
    // with the world as it was

    // TODO: Buffer states here for smooth playback with fluctuating ping
    // For now, just flush all (expect some stuttering in mp)
    for (input <- serverInput) {
      gameUpdater.update(input)
    }

    gameUpdater.predict(ownInput)

  }

}
