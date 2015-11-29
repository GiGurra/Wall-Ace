package se.gigurra.wallace.client.stage.world.renderer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.utils.Align
import se.gigurra.wallace.util.Disposing

case class RenderContext[AssetsType : Disposing](_assets: AssetsType) {

  def fps = Gdx.graphics.getFramesPerSecond

  val state = new RenderState
  val assets: AssetsType = _assets
  val glShortcuts = com.badlogic.gdx.Gdx.gl
  val transform = state.transform
  val batch = state.batch

  def aspect = Gdx.graphics.getWidth.toFloat / Gdx.graphics.getHeight.toFloat

  def maxAspect = math.max(aspect, 1.0f / aspect)

  def prepText(font: Font,
               str: CharSequence,
               align: Int = Align.left,
               targetWidth: Float = 0.0f,
               wrap: Boolean = false): RichGlyphLayout = {
    font.prep(str, align, targetWidth, wrap)
  }

  def drawShapes[AnyReturnType](shapeType: ShapeType)
                               (impl: ShapeRenderer => AnyReturnType): AnyReturnType = {
    state.shapeRenderer.begin(shapeType)
    val out = impl(state.shapeRenderer)
    state.shapeRenderer.end()
    out
  }

  def close(): Unit = {
    implicitly[Disposing[AssetsType]].dispose(assets)
  }

}
