package se.gigurra.wallace.gamemodel

import se.gigurra.wallace.util.Time

object WorldUpdater {

  def create(timeStep: Long,
             isSinglePlayer: Boolean): WorldUpdater = {
    if (isSinglePlayer)
      SinglePlayerWorldUpdater(timeStep)
    else
      MultiPlayerWorldUpdater(timeStep)
  }
}

abstract class WorldUpdater(val timeStep: Time.Milliseconds) {

  /////////////////////////////
  // Data
  //

  private var _lastWorldTime: Time.Milliseconds = Time.millis


  /////////////////////////////
  // API
  //

  def update(world: World[_],
             worldUpdatesQue: WorldUpdatesQue,
             worldFrameUpdater: WorldFrameUpdater,
             eventReceiver: WorldEventReceiver)

  def lastWorldTime: Time.Milliseconds = _lastWorldTime


  /////////////////////////////
  // Helpers
  //

  protected def updateOneFrame(world: World[_],
                               worldUpdatesQue: WorldUpdatesQue,
                               worldFrameUpdater: WorldFrameUpdater)(implicit eventReceiver: WorldEventReceiver): Unit = {
    worldFrameUpdater.update(world, worldUpdatesQue.pop(world.iSimFrame))
    _lastWorldTime += timeStep
    world.iSimFrame += 1
  }

}

case class SinglePlayerWorldUpdater(_timeStep: Time.Milliseconds)
  extends WorldUpdater(_timeStep) {

  override def update(world: World[_],
                      worldUpdatesQue: WorldUpdatesQue,
                      worldFrameUpdater: WorldFrameUpdater,
                      eventReceiver: WorldEventReceiver): Unit = {

    while (lastWorldTime + timeStep < Time.millis) {
      updateOneFrame(world, worldUpdatesQue, worldFrameUpdater)(eventReceiver)
    }
  }
}

case class MultiPlayerWorldUpdater(_timeStep: Time.Milliseconds)
  extends WorldUpdater(_timeStep) {

  override def update(world: World[_],
                      worldUpdatesQue: WorldUpdatesQue,
                      worldFrameUpdater: WorldFrameUpdater,
                      eventReceiver: WorldEventReceiver): Unit = {

    while (worldUpdatesQue.nonEmpty) {
      updateOneFrame(world, worldUpdatesQue, worldFrameUpdater)(eventReceiver)
    }
  }
}
