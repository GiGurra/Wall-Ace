package se.gigurra.wallace.client.renderer

trait RenderAsset {
  def dispose(): Unit
  def upload(): Unit
}
