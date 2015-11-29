package se.gigurra.wallace.util

case class Vec2FixedPoint(var x: Int = 0, var y: Int = 0) {
  import Vec2FixedPoint._

  def +(other: Vec2FixedPoint) = Vec2FixedPoint(x + other.x, y + other.y)
  def -(other: Vec2FixedPoint) = Vec2FixedPoint(x - other.x, y - other.y)
  def isWithin(maxDelta: Int, otherPosition: Vec2FixedPoint) = distance(this, otherPosition) <= maxDelta
  def *(other: Vec2FixedPoint) = x * other.x + y * other.y
  def *(k: Int) = Vec2FixedPoint(x * k, y * k)
  def /(d: Int) = Vec2FixedPoint(x / d, y / d)
  def +(d: Int) = Vec2FixedPoint(x + d, y + d)
  def -(d: Int) = Vec2FixedPoint(x - d, y - d)
  def length = distance(zero, this)
  def unary_- : Vec2FixedPoint = this * (-1)
}

object Vec2FixedPoint {

  def zero = Vec2FixedPoint(0, 0)

  def distance(a: Vec2FixedPoint, b: Vec2FixedPoint): Int = {
    val dx = a.x - b.x
    val dy = a.y - b.y
    val sumSquare = dx * dx + dy * dy
    FixedPoint.sqrt(sumSquare)
  }

  implicit class RichIntForWorldVector(val x: Int) extends AnyVal {
    def *(v: Vec2FixedPoint) = v * x
    def +(v: Vec2FixedPoint) = Vec2FixedPoint(x, x) + v
    def -(v: Vec2FixedPoint) = Vec2FixedPoint(x, x) - v
  }
}