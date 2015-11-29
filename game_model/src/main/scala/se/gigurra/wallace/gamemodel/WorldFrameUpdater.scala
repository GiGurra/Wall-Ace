package se.gigurra.wallace.gamemodel

case class WorldFrameUpdater() {

  def update(state: World[_],
             externalUpdates: Seq[WorldUpdate])
            (implicit eventReceiver: WorldEventReceiver): Unit = {

    applyExternalUpdates(state, externalUpdates)
    runSimulationFrame(state)
  }

  private def applyExternalUpdates(state: World[_],
                                   externalUpdates: Seq[WorldUpdate])
                                  (implicit eventReceiver: WorldEventReceiver): Unit = {
    for (update <- externalUpdates) {
      update.apply(state)
    }
  }

  private def runSimulationFrame(state: World[_])
                                (implicit eventReceiver: WorldEventReceiver): Unit = {

    // TODO: Accelerate, move, explode, yadayada

  }
}
