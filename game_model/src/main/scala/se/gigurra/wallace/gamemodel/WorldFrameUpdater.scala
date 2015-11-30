package se.gigurra.wallace.gamemodel

case class WorldFrameUpdater(worldMode: WorldMode) {

  def update(world: World[_],
             externalUpdates: Seq[WorldUpdate])
            (implicit eventReceiver: WorldEventReceiver): Unit = {

    applyExternalUpdates(world, externalUpdates)
    runSimulationFrame(world)
  }

  private def applyExternalUpdates(world: World[_],
                                   externalUpdates: Seq[WorldUpdate])
                                  (implicit eventReceiver: WorldEventReceiver): Unit = {
    for (update <- externalUpdates) {
      update.apply(world)
    }
  }

  private def runSimulationFrame(world: World[_])
                                (implicit eventReceiver: WorldEventReceiver): Unit = {

    // TODO: Accelerate, move, explode, yadayada
    val updates = worldMode.simulate()
    for (update <- updates) {
      update.apply(world)
    }

  }
}
