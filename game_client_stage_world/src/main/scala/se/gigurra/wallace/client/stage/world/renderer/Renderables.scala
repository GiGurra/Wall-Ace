package se.gigurra.wallace.client.stage.world.renderer

import se.gigurra.wallace.gamemodel.Terrain

object Renderables {

  implicit val spriteRendering: Rendering[Sprite] = new Rendering[Sprite] {
    override def buildRenderAsset[AssetsType](sprite: Sprite)(implicit renderContext: RenderContext[AssetsType]) = {
      new RenderAsset[Sprite] {
        override def draw[AssetsType]()(implicit renderContext: RenderContext[AssetsType]) = renderContext.state.batch.draw(sprite, 0, 0, sprite.width, sprite.height)
        override def dispose(): Unit = sprite.dispose()
        override def upload(): Unit = sprite.upload()
        override def height: Float = sprite.height
        override def width: Float = sprite.width
      }
    }
  }

  implicit val textRendering: Rendering[RichGlyphLayout] = new Rendering[RichGlyphLayout] {
    override def buildRenderAsset[AssetsType](text: RichGlyphLayout)(implicit renderContext: RenderContext[AssetsType]) = {
      new RenderAsset[RichGlyphLayout] {
        override def draw[AssetsType]()(implicit renderContext: RenderContext[AssetsType]) = text.font.draw(renderContext.state.batch, text.layout, 0, 0)
        override def height: Float = text.height
        override def width: Float = text.width
      }
    }
  }

  implicit def terrianRendering[T : Rendering]: Rendering[Terrain[T]] = new Rendering[Terrain[T]] {
    override def buildRenderAsset[AssetsType](t: Terrain[T])(implicit renderContext: RenderContext[AssetsType]) = {
      val backingAsset = implicitly[Rendering[T]].buildRenderAsset(t.storage)(renderContext)
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
