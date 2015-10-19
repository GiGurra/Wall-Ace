package se.gigurra.wallace.model

abstract class Entity(var position: WorldVector = WorldVector()) {

  def isWithin(maxDelta: Int, otherPosition: WorldVector): Boolean = {
    position.isWithin(maxDelta, otherPosition)
  }
}
