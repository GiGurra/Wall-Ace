package se.gigurra.wallace

import se.gigurra.wallace.gamestate.ServerStepMessage

class GameLoop(
  gamestateUpdater: IGameUpdater,
  inputIfc: IGameloopInput,
  timeKeeper: TimeKeeper) {

  def step() {

    val ownInput = inputIfc.getLocalPlayerCurrentInput()
    val lastServerInput = inputIfc.getNewInputsFromServer().lastOption
    val time = timeKeeper.update(lastServerInput)

    gamestateUpdater.update(ownInput, lastServerInput, time)

  }

}
