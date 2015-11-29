package se.gigurra.wallace.gamemodel

trait WorldUpdate {

  def emit(event: WorldEvent)
          (implicit receiver: WorldEventReceiver): Unit = {
    se.gigurra.wallace.gamemodel.emit(event)
  }

  def apply(world: World[_])(implicit eventReceiver: WorldEventReceiver)
}
