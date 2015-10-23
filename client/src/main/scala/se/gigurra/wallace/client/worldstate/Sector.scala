package se.gigurra.wallace.client.worldstate

import se.gigurra.wallace.client.renderer.{RenderAsset, Sprite}
import se.gigurra.wallace.gamemodel.World

class Sector(width: Int, height: Int) extends RenderAsset {

  val asset = Sprite.fromSize(
    width,
    height,
    useMipMaps = true
  )

  val model = new World(new SingleSpriteWorldStore(asset))

  override def dispose() = asset.dispose()

  override def upload(): Unit = asset.upload()

}
