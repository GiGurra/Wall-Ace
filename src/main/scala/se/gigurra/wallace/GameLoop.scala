package se.gigurra.wallace

class GameLoop(
  gamestateUpdater: IGameUpdater,
  inputIfc: IGameloopInput) {

  private var localIterationIndex = 0L
  private var lastLocalTime = calcLocalTime

  def calcLocalTime(): Double = {
    System.nanoTime() / 1e9
  }

  def run() {

    val localPlayerCurrentInput = inputIfc.getLocalPlayerCurrentInput()
    val serverInputs = inputIfc.getNewInputsFromServer()
    val localTime = calcLocalTime()

    gamestateUpdater.update(
      localPlayerCurrentInput,
      serverInputs,
      localIterationIndex,
      localTime,
      lastLocalTime)

    lastLocalTime = localTime
    localIterationIndex += 1L

  }

}