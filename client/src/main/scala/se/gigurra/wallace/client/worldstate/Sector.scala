package se.gigurra.wallace.client.worldstate

import java.nio.ByteBuffer

import se.gigurra.wallace.client.renderer.{Sprite, RenderAsset}
import se.gigurra.wallace.gamemodel.{TerrainData, World}

class Sector(width: Int, height: Int) extends RenderAsset {

  val asset = Sprite.fromSize(
    width,
    height,
    useMipMaps = true
  )

  val model = new World(new TerrainData {
    override def data: ByteBuffer = asset.pixels
    override def width: Int = asset.width
    override def height: Int = asset.height
  })

  override def dispose() = asset.dispose()

  override def upload(): Unit = asset.upload()

}
