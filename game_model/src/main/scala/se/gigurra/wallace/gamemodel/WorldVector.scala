package se.gigurra.wallace.gamemodel

import se.gigurra.wallace.gamemodel

object WorldVector {
  import gamemodel.WorldVector
  def apply(x: Long = 0, y: Long = 0): WorldVector = new WorldVector(x, y)
}