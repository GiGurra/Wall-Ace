package se.gigurra.wallace.client.stage.world.renderer.terrainstorage

import se.gigurra.wallace.client.stage.world.renderer.Sprite
import se.gigurra.wallace.gamemodel.{TerrainPatch, TerrainStorageFactory, TerrainStoring}

object SpriteTerrainStoring {

  implicit val terrainStoringSprite = new TerrainStoring[Sprite] {

    override def width(t: Sprite): Long = t.width

    override def height(t: Sprite): Long = t.height

    override def set(t: Sprite, iPatch: Long, patch: TerrainPatch): Unit = {
      val data = t.data
      val offset = (iPatch * 4).toInt
      data
        .put(offset + 0, patch.r)
        .put(offset + 1, patch.g)
        .put(offset + 2, patch.b)
        .put(offset + 3, patch.a)
    }

    override def get(t: Sprite, iPatch: Long): TerrainPatch = {
      val data = t.data
      val offset = (iPatch * 4).toInt
      TerrainPatch(
        data.get(offset + 0),
        data.get(offset + 1),
        data.get(offset + 2),
        data.get(offset + 3))
    }

  }

}

object SpriteTerrainStorageFactory extends TerrainStorageFactory[Sprite] {
  def create(width: Long, height: Long): Sprite = {
    Sprite.fromSize(width = width.toInt, height = height.toInt, useMipMaps = true)
  }
}
