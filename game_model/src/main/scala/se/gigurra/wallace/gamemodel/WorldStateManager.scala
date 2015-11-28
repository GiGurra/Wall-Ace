package se.gigurra.wallace.gamemodel

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


case class WorldStateManager[T_TerrainStorage: TerrainStoring](terrainStorageFactory: TerrainStorageFactory[T_TerrainStorage],
                                                               dt: Int,
                                                               isSinglePlayer: Boolean) {

  val state = World.create(terrainStorageFactory, 640, 640)
  private var _iSimFrame: Long = 0L
  private var lastWorldTime = systemTime
  private val queuedExternalUpdates = new mutable.HashMap[WorldSimFrameIndex, WorldUpdateBatch]

  def iSimFrame: Long = _iSimFrame

  def update(externalUpdates: Seq[WorldUpdateBatch]): Seq[WorldEvent] = {

    val worldStateEvents = new ArrayBuffer[WorldEvent]()

    queueUpdates(externalUpdates)

    if (isSinglePlayer) {
      while (lastWorldTime + dt < systemTime) {
        worldStateEvents ++= doUpdate(popExternalUpdates(iSimFrame))
      }
    } else {
      while (externalUpdates.nonEmpty) {
        worldStateEvents ++= doUpdate(popExternalUpdates(iSimFrame))
      }
    }

    worldStateEvents
  }

  private def isMultiPlayer = !isSinglePlayer

  private def queueUpdates(worldUpdates: Seq[WorldUpdateBatch]): Unit = {

    for (worldUpdate <- worldUpdates) {

      if (worldUpdate.iSimFrame < iSimFrame)
        throw new RuntimeException("Attempted to queue up external world updates in the past!")

      val alreadyQueuedUpdatesForFrame = queuedExternalUpdates.get(worldUpdate.iSimFrame)

      val updatesForThisFrame = alreadyQueuedUpdatesForFrame match {
        case Some(previousInputs) if isMultiPlayer => throw new RuntimeException(s"Server tried to update frame $iSimFrame twice")
        case Some(previousInputs) => worldUpdate.copy(updates = worldUpdate.updates ++ previousInputs.updates)
        case None => worldUpdate
      }

      queuedExternalUpdates.put(worldUpdate.iSimFrame, updatesForThisFrame)
    }
  }

  private def systemTime: Long = {
    System.currentTimeMillis()
  }

  private def popExternalUpdates(iSimFrame: Long): WorldUpdateBatch = {
    queuedExternalUpdates.remove(iSimFrame).getOrElse(WorldUpdateBatch(iSimFrame, Seq.empty))
  }

  private def doUpdate(externalUpdates: WorldUpdateBatch): Seq[WorldEvent] = {

    val worldStateEvents = new ArrayBuffer[WorldEvent]()

    worldStateEvents ++= applyExternalUpdates(externalUpdates)
    worldStateEvents ++= runSimulationFrame()

    lastWorldTime += dt
    _iSimFrame += 1

    worldStateEvents
  }

  private def applyExternalUpdates(externalUpdates: WorldUpdateBatch): Seq[WorldEvent] = {

    if (iSimFrame != externalUpdates.iSimFrame)
      throw new RuntimeException("Attempted to apply external world updates with wrong sim frame index")

    val events = externalUpdates.updates.flatMap(_.apply(state))

    events
  }

  private def runSimulationFrame(): Seq[WorldEvent] = {

    val worldStateEvents = new ArrayBuffer[WorldEvent]()

    // TODO: Accelerate, move, explode, yadayada

    worldStateEvents
  }

}
