package se.gigurra.wallace.gamemodel

import scala.collection.mutable

case class WorldUpdatesQue() {

  private val queuedExternalUpdates = new mutable.HashMap[WorldSimFrameIndex, Seq[WorldUpdate]]

  def pop(iFrame: WorldSimFrameIndex): Seq[WorldUpdate] = {
    queuedExternalUpdates.remove(iFrame).getOrElse(Seq.empty)
  }

  def queueUpdates(iCurrentFrame: WorldSimFrameIndex, worldUpdates: Seq[WorldUpdateBatch]): Unit = {

    for (worldUpdate <- worldUpdates) {

      if (worldUpdate.iSimFrame < iCurrentFrame)
        throw new RuntimeException("Attempted to queue up external world updates in the past!")

      val alreadyQueuedUpdatesForFrame = queuedExternalUpdates.get(worldUpdate.iSimFrame)

      val updatesForThisFrame = alreadyQueuedUpdatesForFrame match {
        case Some(previousInputs) => worldUpdate.updates ++ previousInputs
        case None => worldUpdate.updates
      }

      queuedExternalUpdates.put(worldUpdate.iSimFrame, updatesForThisFrame)
    }
  }

  def nonEmpty: Boolean = {
    queuedExternalUpdates.nonEmpty
  }

}
