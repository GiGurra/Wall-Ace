package se.gigurra.wallace.gamemodel

/**
  * Used to forward non-persistent information to renderers etc
  */
sealed trait WorldEvent

case class PlayerSpawn(id: String,
                       name: String,
                       position: WorldVector) extends WorldEvent {

}