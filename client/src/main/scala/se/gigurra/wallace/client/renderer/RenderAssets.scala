package se.gigurra.wallace.client.renderer

import scala.collection.mutable

class RenderAssetsCategory[T <: RenderAsset] {
  private val data = new mutable.HashMap[String, T]()

  def ensureHas(id: String, factory: => T): RenderAssetsCategory[T] = {
    getOrElseUpdate(id, factory)
    this
  }

  def getOrElseUpdate(id: String, factory: => T): T = {
    data.getOrElseUpdate(id, {
      val asset = factory
      asset.upload()
      asset
    })
  }

  def apply(id: String): T = {
    data.apply(id)
  }

  def delete(id: String): RenderAssetsCategory[T] = {
    data.remove(id).foreach(_.dispose())
    this
  }

  def replace(id: String, asset: T = null.asInstanceOf[T]): RenderAssetsCategory[T] = {
    data.get(id) match {
      case Some(prevAsset) if (asset eq prevAsset) =>
      case _ =>
        delete(id)
        if(asset != null)
          ensureHas(id, asset)
    }
    this
  }
}

class RenderAssets {
  val font20 = Font.fromTtfFile("fonts/pt-mono/PTM55FT.ttf", size = 40)
  val libgdxLogo = Sprite.fromFile("libgdxlogo.png", useMipMaps = false)

  val maps = new RenderAssetsCategory[RenderAsset]

}
