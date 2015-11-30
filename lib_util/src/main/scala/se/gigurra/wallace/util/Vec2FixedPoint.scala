package se.gigurra.wallace.util

case class Vec2FixedPoint(x: Long = 0L, y: Long = 0L) {
  import Vec2FixedPoint._

  def +(other: Vec2FixedPoint) = Vec2FixedPoint(x + other.x, y + other.y)
  def -(other: Vec2FixedPoint) = Vec2FixedPoint(x - other.x, y - other.y)
  def isWithin(maxDelta: Long, otherPosition: Vec2FixedPoint) = distance(this, otherPosition) <= maxDelta
  def *(other: Vec2FixedPoint) = x * other.x + y * other.y
  def *(k: Long): Vec2FixedPoint = Vec2FixedPoint(x * k, y * k)
  def /(d: Long): Vec2FixedPoint = {
    require(d != 0, "Cannot divide vector by zero")
    Vec2FixedPoint(x / d, y / d)
  }
  def +(d: Long): Vec2FixedPoint = Vec2FixedPoint(x + d, y + d)
  def -(d: Long): Vec2FixedPoint = Vec2FixedPoint(x - d, y - d)
  def length = distance(zero, this)
  def unary_- : Vec2FixedPoint = this * (-1)
  def isZero = x == 0L && y == 0L
  def normalized(newLength: Long, acceptZeroLength: Boolean): Vec2FixedPoint = {
    if (isZero && acceptZeroLength) {
      Vec2FixedPoint.zero
    } else {
      Vec2FixedPoint.normalized(this, newLength)
    }
  }
}

object Vec2FixedPoint {

  val zero = Vec2FixedPoint(0L, 0L)

  def normalized(_v: Vec2FixedPoint, newLength: Long): Vec2FixedPoint = {
    require(newLength < Int.MaxValue / 2, "Arithmetic issue - cannot normalize to lenth > Int.MaxValue / 2")
    require(_v.length < Int.MaxValue / 2, "Arithmetic issue - cannot normalize if v.length > Int.MaxValue / 2")
    require(!_v.isZero, "Cannot normalize a zero vector")

    val upScale = Int.MaxValue / 4 / _v.length
    val v = _v * upScale
    v * newLength / v.length
  }

  def distance(a: Vec2FixedPoint, b: Vec2FixedPoint): Long = {

    val dx = a.x - b.x
    val dy = a.y - b.y

    require(dx < Int.MaxValue / 2, "Arithmetic issue - cannot normalize to dx > Int.MaxValue / 2")
    require(dy < Int.MaxValue / 2, "Arithmetic issue - cannot normalize to dx > Int.MaxValue / 2")

    val sumSquare = dx * dx + dy * dy
    FixedPoint.sqrt(sumSquare)
  }

  implicit class RichLongForWorldVector(val x: Long) extends AnyVal {
    def *(v: Vec2FixedPoint) = v * x
    def +(v: Vec2FixedPoint) = Vec2FixedPoint(x, x) + v
    def -(v: Vec2FixedPoint) = Vec2FixedPoint(x, x) - v
  }

  implicit class RichIntForWorldVector(val x: Int) extends AnyVal {
    def *(v: Vec2FixedPoint) = v * x.toLong
    def +(v: Vec2FixedPoint) = Vec2FixedPoint(x.toLong, x.toLong) + v
    def -(v: Vec2FixedPoint) = Vec2FixedPoint(x.toLong, x.toLong) - v
  }
}