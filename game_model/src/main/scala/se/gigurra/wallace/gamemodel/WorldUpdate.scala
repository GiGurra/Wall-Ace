package se.gigurra.wallace.gamemodel

import se.gigurra.wallace.util.Decorated

trait WorldUpdate {

  def emit(event: WorldEvent)
          (implicit receiver: WorldEventReceiver): Unit = {
    se.gigurra.wallace.gamemodel.emit(event)
  }

  def apply(world: World[_])(implicit eventReceiver: WorldEventReceiver)
}

object WorldUpdate {

  case class RichWorld(state: World[_], eventReceiver: WorldEventReceiver) extends Decorated[World[_]](state)

  def apply(f: (World[_], WorldEventReceiver) => Unit): WorldUpdate = {
    new WorldUpdate {
      override def apply(world: World[_])(implicit eventReceiver: WorldEventReceiver): Unit = {
        f(world, eventReceiver)
      }
    }
  }

  def apply(f: RichWorld => Unit): WorldUpdate = {
    new WorldUpdate {
      override def apply(world: World[_])(implicit eventReceiver: WorldEventReceiver): Unit = {
        f(RichWorld(world, eventReceiver))
      }
    }
  }

}

