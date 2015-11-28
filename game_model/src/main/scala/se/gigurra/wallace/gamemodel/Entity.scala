package se.gigurra.wallace.gamemodel

abstract class Entity(id: String, var position: WorldVector = new WorldVector()) {

  def isWithin(maxDelta: Int, otherPosition: WorldVector): Boolean = {
    position.isWithin(maxDelta, otherPosition)
  }
}
