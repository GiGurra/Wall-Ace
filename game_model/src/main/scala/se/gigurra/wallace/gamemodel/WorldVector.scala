package se.gigurra.wallace.gamemodel

import se.gigurra.wallace.gamemodel

object WorldVector {
  import gamemodel.WorldVector
  def apply(x: Int = 0, y: Int = 0): WorldVector = new WorldVector(x, y)
}