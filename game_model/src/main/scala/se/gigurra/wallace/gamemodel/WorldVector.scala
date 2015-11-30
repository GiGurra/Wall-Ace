package se.gigurra.wallace.gamemodel

import se.gigurra.wallace.gamemodel

object WorldVector {

  val zero = WorldVector(0L, 0L)

  import gamemodel.WorldVector
  def apply(x: Long = 0, y: Long = 0): WorldVector = new WorldVector(x, y)
}