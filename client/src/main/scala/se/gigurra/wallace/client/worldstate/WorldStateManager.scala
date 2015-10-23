package se.gigurra.wallace.client.worldstate

import se.gigurra.wallace.gamemodel.TerrainGenerator

class WorldStateManager {
  import WorldStateManager._
  var sector: Option[Sector] = Some(makeDefaultWorld())

  def update(): Unit = {

  }
}

object WorldStateManager {
  def makeDefaultWorld(): Sector = {
    // No need to delete here
    // The renderers assets management will
    // when the world sprite is replaced
    val width = 640
    val height = 640
    val out = new Sector(width, height)
    TerrainGenerator.generate("MyMapSeed", out.model)
    out
  }
}
