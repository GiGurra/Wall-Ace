package se.gigurra.renderer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Align

case class RenderContext[AssetsType](_assets: AssetsType) {

  def fps = Gdx.graphics.getFramesPerSecond

  val state = new RenderState
  val assets: AssetsType = _assets
  val glShortcuts = com.badlogic.gdx.Gdx.gl

  def aspect = Gdx.graphics.getWidth.toFloat / Gdx.graphics.getHeight.toFloat

  def maxAspect = math.max(aspect, 1.0f / aspect)

  object drawShortcuts {

    def drawSprite(sprite: Sprite, x: Float, y: Float, w: Float, h: Float) {
      state.batch.draw(sprite, x, y, w, h)
    }

    def drawSprite(sprite: Sprite, x: Float = 0.0f, y: Float = 0.0f) {
      drawSprite(sprite, x, y, sprite.getWidth.toFloat, sprite.getHeight.toFloat)
    }

    def drawText(text: RichGlyphLayout, x: Float = 0.0f, y: Float = 0.0f): Unit = {
      text.font.draw(state.batch, text, x, y)
    }

    def prepText(font: Font,
                 str: CharSequence,
                 align: Int = Align.left,
                 targetWidth: Float = 0.0f,
                 wrap: Boolean = false): RichGlyphLayout = {
      font.prep(str, align, targetWidth, wrap)
    }

  }

}
