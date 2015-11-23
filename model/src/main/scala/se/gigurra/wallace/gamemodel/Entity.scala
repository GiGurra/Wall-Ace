package se.gigurra.wallace.gamemodel

abstract class Entity(var position: WorldVector = WorldVector()) {

  def isWithin(maxDelta: Int, otherPosition: WorldVector): Boolean = {
    position.isWithin(maxDelta, otherPosition)
  }
}
