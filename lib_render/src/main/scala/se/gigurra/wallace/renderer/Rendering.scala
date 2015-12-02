package se.gigurra.wallace.renderer

trait Rendering[Renderable] {
  def buildRenderAsset[AssetsType](renderable: Renderable)(implicit renderContext: RenderContext[AssetsType]): RenderAsset[Renderable]
}
