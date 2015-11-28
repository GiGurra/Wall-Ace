package se.gigurra.wallace.gamemodel

import scala.collection.mutable.ArrayBuffer

case class WorldFrameUpdater() {

  def update(state: World[_],
             externalUpdates: Seq[WorldUpdate]): Seq[WorldEvent] = {

    val worldStateEvents = new ArrayBuffer[WorldEvent]()

    worldStateEvents ++= applyExternalUpdates(state, externalUpdates)
    worldStateEvents ++= runSimulationFrame(state)

    worldStateEvents
  }

  private def applyExternalUpdates(state: World[_],
                                   externalUpdates: Seq[WorldUpdate]): Seq[WorldEvent] = {

    val events = externalUpdates.flatMap(_.apply(state))

    events
  }

  private def runSimulationFrame(state: World[_]): Seq[WorldEvent] = {

    val worldStateEvents = new ArrayBuffer[WorldEvent]()

    // TODO: Accelerate, move, explode, yadayada

    worldStateEvents
  }
}
