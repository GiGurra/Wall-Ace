package se.gigurra.wallace.client.worldstate

import se.gigurra.wallace.client.renderer.Sprite
import se.gigurra.wallace.gamemodel.{Terrain, TerrainStore}

class SingleSpriteTerrainStore(sprite: Sprite) extends TerrainStore {

  private val terrainData = sprite.pixels

  override def width: Int = sprite.width

  override def height: Int = sprite.height

  override def get(x: Int, y: Int): Terrain = {
    val i = terrainIndexOf(x,y) * 4
    Terrain(
      terrainData.get(i + 0),
      terrainData.get(i + 1),
      terrainData.get(i + 2),
      terrainData.get(i + 3))
  }

  override def set(x: Int, y: Int, value: Terrain): Unit = {
    val i = terrainIndexOf(x,y) * 4
    terrainData
      .put(i + 0, value.r)
      .put(i + 1, value.g)
      .put(i + 2, value.b)
      .put(i + 3, value.a)
  }

}
