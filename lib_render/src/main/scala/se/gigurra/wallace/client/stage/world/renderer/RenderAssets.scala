package se.gigurra.wallace.client.stage.world.renderer

import scala.collection.mutable

class RenderAssetsCategory[SourceType] {
  private val data = new mutable.HashMap[String, RenderAsset[SourceType]]()

  def ensureHas[T1 <: SourceType : Rendering, AssetsType](id: String, source: T1)(implicit renderContext: RenderContext[AssetsType]): RenderAssetsCategory[SourceType] = {
    getOrElseUpdate(id, source)
    this
  }

  def getOrElseUpdate[T1 <: SourceType : Rendering, AssetsType](id: String, source: T1)(implicit renderContext: RenderContext[AssetsType]): RenderAsset[SourceType] = {
    getOrElseUpdate(id, implicitly[Rendering[T1]].buildRenderAsset(source))
  }

  def ensureHas(id: String, asset: => RenderAsset[SourceType]): RenderAssetsCategory[SourceType] = {
    getOrElseUpdate(id, asset)
    this
  }

  def getOrElseUpdate(id: String, asset: => RenderAsset[SourceType]): RenderAsset[SourceType] = {
    data.getOrElseUpdate(id, asset)
  }

  def apply(id: String): RenderAsset[SourceType] = {
    data.apply(id)
  }

  def delete(id: String): RenderAssetsCategory[SourceType] = {
    data.remove(id).foreach(_.dispose())
    this
  }

  def replace[SourceType1 <: SourceType : Rendering, AssetsType](id: String, asset: RenderAsset[SourceType1] = null.asInstanceOf[RenderAsset[SourceType]])(implicit renderContext: RenderContext[AssetsType]): RenderAssetsCategory[SourceType] = {
    data.get(id) match {
      case Some(prevAsset) if (asset eq prevAsset) =>
      case _ =>
        delete(id)
        if (asset != null)
          ensureHas(id, asset)
    }
    this
  }
}

class RenderAssets {
  val font20 = Font.fromTtfFile("fonts/pt-mono/PTM55FT.ttf", size = 40)
  val libgdxLogo = Sprite.fromFile("libgdxlogo.png", useMipMaps = false)

  val maps = new RenderAssetsCategory[AnyRef]
  val sprites = new RenderAssetsCategory[Sprite]

  def temporary[T: Rendering : Manifest, AssetsType](t: T)(implicit renderContext: RenderContext[AssetsType]) = {
    import resource._
    implicit val x = manifest[Rendering[T]]
    managed(implicitly[Rendering[T]].buildRenderAsset(t))
  }

}
