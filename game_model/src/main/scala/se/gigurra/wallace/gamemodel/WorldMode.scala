package se.gigurra.wallace.gamemodel

import se.gigurra.wallace.util.Vec2FixedPoint

import scala.util.Random

// World modes must be stateless
// Any state that needs to be saved will be done so in the World object
trait WorldMode {

  def name: String

  // The world mode may issue requests, like spawn own player etc
  def createRequests(playerUnitId: Option[String]): Seq[WorldUpdate]

  // Must never have any knowledge of the current player
  // This simulation will execute identically on all clients and the server
  def simulate(): Seq[WorldUpdate]
}

case class SandboxMode() extends WorldMode {

  override val name: String = "sandbox"

  override def createRequests(playerUnitId: Option[String]): Seq[WorldUpdate] = {

    def spawnOwnCharacter(playerUnitId: String): WorldUpdate = {
      new WorldUpdate {
        override def apply(world: World[_])(implicit eventReceiver: WorldEventReceiver): Unit = {
          world.getEntity(playerUnitId) match {
            case Some(ownEntity) => // No need - already exists
            case None =>
              val sz = world.size
              val r = new Random()
              val x = sz.x / 4 + r.nextInt(sz.x.toInt / 2)
              val y = sz.y / 4 + r.nextInt(sz.y.toInt / 2)

              world.addEntity(Entity(
                id = playerUnitId,
                isPlayerUnit = true,
                position = WorldVector(x,y)
              ))
          }
        }
      }
    }

    playerUnitId.map(spawnOwnCharacter).toSeq
  }

  override def simulate(): Seq[WorldUpdate] = {
    // Sandbox mode.. nothing special
    Seq.empty
  }

}

object WorldMode {

  val availableModes: Map[String, WorldMode] = Seq(SandboxMode()).map(m => (m.name, m)).toMap

  def get(modeName: String): WorldMode = {
    require(availableModes.contains(modeName), s"Unknown world mode name ${modeName}")
    availableModes(modeName)
  }

}