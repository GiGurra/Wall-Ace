package se.gigurra.wallace.model

case class WorldVector(var x: Int = 0, var y: Int = 0) {

  def +(other: WorldVector) = new WorldVector(x + other.x, y + other.y)

  def -(other: WorldVector) = new WorldVector(x - other.x, y - other.y)

  def isWithin(maxDelta: Int, otherPosition: WorldVector): Boolean = {
    val delta = this - otherPosition
    math.abs(delta.x) <= maxDelta && math.abs(delta.y) <= maxDelta
  }

}
