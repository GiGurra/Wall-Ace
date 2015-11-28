package se.gigurra.wallace.gamemodel

case class WorldFrameUpdater() {

  def update(state: World[_],
             externalUpdates: Seq[WorldUpdate])
            (implicit emit: WorldEventReceiver): Unit = {

    applyExternalUpdates(state, externalUpdates)
    runSimulationFrame(state)
  }

  private def applyExternalUpdates(state: World[_],
                                   externalUpdates: Seq[WorldUpdate])
                                  (implicit emit: WorldEventReceiver): Unit = {

    for (update <- externalUpdates) {
      val events = update.apply(state)
      for (event <- events) {
        emit(event)
      }
    }
  }

  private def runSimulationFrame(state: World[_])
                                (implicit emit: WorldEventReceiver): Unit = {

    // TODO: Accelerate, move, explode, yadayada

  }
}
