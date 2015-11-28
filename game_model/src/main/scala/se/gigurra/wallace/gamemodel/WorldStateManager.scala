package se.gigurra.wallace.gamemodel

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

case class WorldStateManager[T_TerrainStorage: TerrainStoring](terrainStorageFactory: TerrainStorageFactory[T_TerrainStorage],
                                                               dt: Int,
                                                               isSinglePlayer: Boolean) {

  val state = World.create(terrainStorageFactory, 640, 640)
  private var _iSimFrame: Long = 0L
  private var lastWorldTime = systemTime
  private val queuedExternalUpdates = new mutable.HashMap[WorldSimFrameIndex, Seq[WorldUpdate]]
  private val worldFrameUpdater = WorldFrameUpdater()

  def iSimFrame: Long = _iSimFrame

  def update(externalUpdates: Seq[WorldUpdateBatch]): Seq[WorldEvent] = {

    val worldStateEvents = new ArrayBuffer[WorldEvent]()

    queueUpdates(externalUpdates)

    if (isSinglePlayer) {
      while (lastWorldTime + dt < systemTime) {
        worldStateEvents ++= doUpdate()
      }
    } else {
      while (queuedExternalUpdates.nonEmpty) {
        worldStateEvents ++= doUpdate()
      }
    }

    worldStateEvents
  }

  private def doUpdate(): Seq[WorldEvent] = {
    val out = worldFrameUpdater.update(state, popExternalUpdates(iSimFrame))
    lastWorldTime += dt
    _iSimFrame += 1
    out
  }

  private def isMultiPlayer = !isSinglePlayer

  private def queueUpdates(worldUpdates: Seq[WorldUpdateBatch]): Unit = {

    for (worldUpdate <- worldUpdates) {

      if (worldUpdate.iSimFrame < iSimFrame)
        throw new RuntimeException("Attempted to queue up external world updates in the past!")

      val alreadyQueuedUpdatesForFrame = queuedExternalUpdates.get(worldUpdate.iSimFrame)

      val updatesForThisFrame = alreadyQueuedUpdatesForFrame match {
        case Some(previousInputs) if isMultiPlayer => throw new RuntimeException(s"Server tried to update frame $iSimFrame twice")
        case Some(previousInputs) => worldUpdate.updates ++ previousInputs
        case None => worldUpdate.updates
      }

      queuedExternalUpdates.put(worldUpdate.iSimFrame, updatesForThisFrame)
    }
  }

  private def systemTime: Long = {
    System.currentTimeMillis()
  }

  private def popExternalUpdates(iSimFrame: Long): Seq[WorldUpdate] = {
    queuedExternalUpdates.remove(iSimFrame).getOrElse(Seq.empty)
  }


}
