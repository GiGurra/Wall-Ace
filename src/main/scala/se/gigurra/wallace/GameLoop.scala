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

    // Get all inputs
    val localPlayerCurrentInput = inputIfc.getLocalPlayerCurrentInput()
    val authorativeSnapshot = inputIfc.getAuthorativeSnapshot()
    val remotePlayersNewInputs = inputIfc.getRemotePlayersNewInputs()

    val localTime = calcLocalTime

    gamestateUpdater.update(
      localPlayerCurrentInput,
      authorativeSnapshot,
      remotePlayersNewInputs,
      localIterationIndex,
      localTime,
      lastLocalTime)

    lastLocalTime = localTime
    localIterationIndex += 1L

  }

}