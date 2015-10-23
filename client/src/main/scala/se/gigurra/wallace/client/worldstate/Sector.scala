package se.gigurra.wallace.client.worldstate

import java.nio.ByteBuffer

import se.gigurra.wallace.client.renderer.{Sprite, RenderAsset}
import se.gigurra.wallace.gamemodel.{Terrain, WorldVector, WorldStore, World}

class Sector(width: Int, height: Int) extends RenderAsset {

  val asset = Sprite.fromSize(
    width,
    height,
    useMipMaps = true
  )

  val terrainData = asset.pixels

  val model = new World(new WorldStore {

    override def width: Int = asset.width

    override def height: Int = asset.height

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
  })

  override def dispose() = asset.dispose()

  override def upload(): Unit = asset.upload()

}
