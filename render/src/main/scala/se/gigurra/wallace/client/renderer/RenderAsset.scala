package se.gigurra.wallace.client.renderer

import java.io.Closeable

trait RenderAsset[+SourceType] extends Closeable {
  def dispose(): Unit = {}
  def upload(): Unit = {}
  def width: Float
  def height: Float
  def draw[AssetsType]()(implicit renderContext: RenderContext[AssetsType]): Unit

  override def close(): Unit = dispose()

  def uploaded(): RenderAsset[SourceType] = {
    upload()
    this
  }
}
