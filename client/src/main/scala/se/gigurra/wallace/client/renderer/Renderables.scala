package se.gigurra.wallace.client.renderer

import se.gigurra.wallace.gamemodel.{Terrain, TerrainStorage}

object Renderables {
  import Renderer._

  implicit val sprite = new Rendering[Sprite] {
    override def buildRenderAsset[AssetsType](sprite: Sprite)(implicit renderContext: RenderContext[AssetsType]) = {
      new RenderAsset[Sprite] {
        override def draw[AssetsType]()(implicit renderContext: RenderContext[AssetsType]) = renderContext.drawShortcuts.drawSprite(sprite)
        override def dispose(): Unit = sprite.dispose()
        override def upload(): Unit = sprite.upload()
        override def height: Float = sprite.height
        override def width: Float = sprite.width
      }
    }
  }

  implicit val text = new Rendering[RichGlyphLayout] {
    override def buildRenderAsset[AssetsType](text: RichGlyphLayout)(implicit renderContext: RenderContext[AssetsType]) = {
      new RenderAsset[RichGlyphLayout] {
        override def draw[AssetsType]()(implicit renderContext: RenderContext[AssetsType]) = renderContext.drawShortcuts.drawText(text)
        override def height: Float = text.height
        override def width: Float = text.width
      }
    }
  }

  implicit def terrianRenderer[T <: TerrainStorage : Rendering] = new Rendering[Terrain[T]] {
    override def buildRenderAsset[AssetsType](t: Terrain[T])(implicit renderContext: RenderContext[AssetsType]) = {
      val backingAsset = t.storage.buildRenderAsset()
      new RenderAsset[Terrain[T]] {
        override def draw[AssetsType]()(implicit renderContext: RenderContext[AssetsType]) = backingAsset.draw()
        override def dispose(): Unit = backingAsset.dispose()
        override def upload(): Unit = backingAsset.upload()
        override def height: Float = backingAsset.height
        override def width: Float = backingAsset.width
      }
    }
  }

}
