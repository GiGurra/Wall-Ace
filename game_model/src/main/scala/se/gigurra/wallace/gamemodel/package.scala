package se.gigurra.wallace

import se.gigurra.wallace.util.Vec2FixedPoint

package object gamemodel {
  type WorldSimFrameIndex = Long
  type WorldVector = Vec2FixedPoint
  type WorldUpdate = (World[_] => Seq[WorldEvent])
}

object WorldVector {
  import gamemodel.WorldVector
  def apply(x: Int = 0, y: Int = 0): WorldVector = new WorldVector(x, y)
}