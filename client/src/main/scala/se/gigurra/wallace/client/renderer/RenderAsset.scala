package se.gigurra.wallace.client.renderer

trait RenderAsset {
  def dispose(): Unit
  def upload(): Unit
  def width: Int
  def height: Int
  def draw[AssetsType]()(implicit renderContext: RenderContext[AssetsType]): Unit
}
