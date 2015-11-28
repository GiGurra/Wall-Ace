package se.gigurra.wallace.gamemodel

import se.gigurra.wallace.util.Time

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

case class WorldStateManager[T_TerrainStorage: TerrainStoring](terrainStorageFactory: TerrainStorageFactory[T_TerrainStorage],
                                                               dt: Int,
                                                               isSinglePlayer: Boolean) {

  val state = World.create(terrainStorageFactory, 640, 640)
  private var _iSimFrame: Long = 0L
  private var _lastWorldTime = Time.millis
  private val queuedExternalUpdates = new mutable.HashMap[WorldSimFrameIndex, Seq[WorldUpdate]]
  private val worldFrameUpdater = WorldFrameUpdater()

  def iSimFrame: Long = _iSimFrame
  def lastWorldTime: Time.Milliseconds = _lastWorldTime

  def update(externalUpdates: Seq[WorldUpdateBatch]): Seq[WorldEvent] = {

    val worldStateEvents = new ArrayBuffer[WorldEvent]()

    queueUpdates(externalUpdates)

    if (isSinglePlayer) {
      while (lastWorldTime + dt < Time.millis) {
        updateOneFrame(eventReceiver = worldStateEvents.+=(_))
      }
    } else {
      while (queuedExternalUpdates.nonEmpty) {
        updateOneFrame(eventReceiver = worldStateEvents.+=(_))
      }
    }

    worldStateEvents
  }

  private def updateOneFrame(implicit eventReceiver: WorldEventReceiver) = {
    val out = worldFrameUpdater.update(state, popExternalUpdates(iSimFrame))
    _lastWorldTime += dt
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

  private def popExternalUpdates(iSimFrame: Long): Seq[WorldUpdate] = {
    queuedExternalUpdates.remove(iSimFrame).getOrElse(Seq.empty)
  }


}
