package se.gigurra.wallace.gamemodel

import se.gigurra.wallace.util.FixedPoint

case class WorldVector(var x: Int = 0, var y: Int = 0) {

  import WorldVector._

  def +(other: WorldVector) = new WorldVector(x + other.x, y + other.y)

  def -(other: WorldVector) = new WorldVector(x - other.x, y - other.y)

  def isWithin(maxDelta: Int, otherPosition: WorldVector) = distance(this, otherPosition) <= maxDelta

  def *(other: WorldVector) = x * other.x + y * other.y

  def *(k: Int) = new WorldVector(x * k, y * k)

  def /(d: Int) = new WorldVector(x / d, y / d)

  def +(d: Int) = new WorldVector(x + d, y + d)

  def -(d: Int) = new WorldVector(x - d, y - d)

  def length = distance(zero, this)
}

object WorldVector {

  def zero = WorldVector(0, 0)

  def distance(a: WorldVector, b: WorldVector): Int = {
    val dx = a.x - b.x
    val dy = a.y - b.y
    val sumSquare = dx * dx + dy * dy
    FixedPoint.sqrt(sumSquare)
  }

  implicit class RichIntForWorldVector(val x: Int) extends AnyVal {

    def *(v: WorldVector) = v * x

    def +(v: WorldVector) = WorldVector(x, x) + v

    def -(v: WorldVector) = WorldVector(x, x) - v
  }

}