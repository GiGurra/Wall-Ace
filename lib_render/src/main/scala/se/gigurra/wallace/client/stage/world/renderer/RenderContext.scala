package se.gigurra.wallace.client.stage.world.renderer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Align
import se.gigurra.wallace.util.Disposing

case class RenderContext[AssetsType : Disposing](_assets: AssetsType) {

  def fps = Gdx.graphics.getFramesPerSecond

  val state = new RenderState
  val assets: AssetsType = _assets
  val glShortcuts = com.badlogic.gdx.Gdx.gl
  val transform = state.transform

  def aspect = Gdx.graphics.getWidth.toFloat / Gdx.graphics.getHeight.toFloat

  def maxAspect = math.max(aspect, 1.0f / aspect)

  def prepText(font: Font,
               str: CharSequence,
               align: Int = Align.left,
               targetWidth: Float = 0.0f,
               wrap: Boolean = false): RichGlyphLayout = {
    font.prep(str, align, targetWidth, wrap)
  }

  def close(): Unit = {
    implicitly[Disposing[AssetsType]].dispose(assets)
  }

}
