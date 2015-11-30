package se.gigurra.wallace.gamemodel

import scala.collection.mutable.ArrayBuffer

case class WorldStateManager[T](timeStep: Int,
                                isSinglePlayer: Boolean,
                                startWorld: World[T],
                                worldMode: WorldMode) {

  ////////////////////////
  // Private state
  //

  private val worldFrameUpdater = WorldFrameUpdater(worldMode)
  private val worldUpdatesQue = WorldUpdatesQue()
  private val worldUpdater = WorldUpdater.create(timeStep, isSinglePlayer)


  ////////////////////////
  // API
  //

  val world = startWorld

  def iSimFrame: WorldSimFrameIndex = world.iSimFrame

  def update(externalUpdates: Seq[WorldUpdateBatch]): Seq[WorldEvent] = {
    val worldStateEvents = new ArrayBuffer[WorldEvent]()
    worldUpdatesQue.queueUpdates(iSimFrame, externalUpdates)
    worldUpdater.update(world, worldUpdatesQue, worldFrameUpdater, worldStateEvents.+=(_))
    world.flushCaches()
    worldStateEvents
  }

}
