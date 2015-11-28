package se.gigurra.wallace.client.renderer

import se.gigurra.wallace.gamemodel.{TerrainStorageFactory, TerrainStoring, TerrainPatch}

object SpriteTerrainStoring {

  implicit val terrainStoringSprite = new TerrainStoring[Sprite] {

    override def width(t: Sprite): Int = t.width

    override def height(t: Sprite): Int = t.height

    override def set(t: Sprite, iPatch: Int, patch: TerrainPatch): Unit = {
      val data = t.data
      val offset = iPatch * 4
      data
        .put(offset + 0, patch.r)
        .put(offset + 1, patch.g)
        .put(offset + 2, patch.b)
        .put(offset + 3, patch.a)
    }

    override def get(t: Sprite, iPatch: Int): TerrainPatch = {
      val data = t.data
      val offset = iPatch * 4
      TerrainPatch(
        data.get(offset + 0),
        data.get(offset + 1),
        data.get(offset + 2),
        data.get(offset + 3))
    }

  }

}

object SpriteTerrainStorageFactory extends TerrainStorageFactory[Sprite] {
  def create(width: Int, height: Int): Sprite = {
    Sprite.fromSize(width = width, height = height, useMipMaps = true)
  }
}
