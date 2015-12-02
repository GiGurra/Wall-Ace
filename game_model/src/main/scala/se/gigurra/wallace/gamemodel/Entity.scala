package se.gigurra.wallace.gamemodel

case class Entity(val id: String,
                  val isPlayerUnit: Boolean,
                  var position: WorldVector = new WorldVector(),
                  // Extensions
                  var name: Option[String] = None,
                  var team: Option[String] = None,
                  var velocity: Option[WorldVector] = None,
                  var acceleration: Option[WorldVector] = None) {

  def isWithin(maxDelta: Int, otherPosition: WorldVector): Boolean = {
    position.isWithin(maxDelta, otherPosition)
  }
}
