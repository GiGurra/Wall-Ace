package se.gigurra.wallace.gamemodel

/**
  * Used to forward non-persistent information to renderers etc
  */
sealed trait WorldEvent {
  def time: WorldSimFrameIndex
  def position: WorldVector
  def radiusOfInterest: Int
}