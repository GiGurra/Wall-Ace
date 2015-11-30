package se.gigurra.wallace.gamemodel

case class WorldFrameUpdater(worldMode: WorldMode, timeStep: Long) {
  import World._

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

    val updates = worldMode.simulate()
    for (update <- updates) {
      update.apply(world)
    }

    // Move
    for (entity <- world.allEntities) {
      val velocity = entity.velocity.getOrElse(WorldVector.zero)
      if (!velocity.isZero)
        entity.position = world.clamp(entity.position + velocity * timeStep)
    }

    // TODO: Accelerate, explode, yadayada

  }
}
