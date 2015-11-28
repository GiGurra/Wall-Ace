package se.gigurra.wallace

import se.gigurra.wallace.util.Vec2FixedPoint

package object gamemodel {
  type WorldSimFrameIndex = Long
  type WorldVector = Vec2FixedPoint
  type WorldUpdate = (World[_] => Seq[WorldEvent])
  type WorldEventReceiver = WorldEvent => Unit

  def emit(event: WorldEvent)
          (implicit receiver: WorldEventReceiver): Unit = {
    receiver.apply(event)
  }
}
