package se.gigurra.wallace.client.stage.world.renderer

import java.io.Closeable

trait Rendering[Renderable] {
  def buildRenderAsset[AssetsType](renderable: Renderable)(implicit renderContext: RenderContext[AssetsType]): RenderAsset[Renderable]
}
