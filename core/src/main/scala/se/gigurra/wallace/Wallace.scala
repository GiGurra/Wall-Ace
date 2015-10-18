package se.gigurra.wallace

import com.badlogic.gdx.graphics.GL20._
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.{Game, Gdx}
import se.gigurra.wallace.render.{Projections, RenderContext}


class Wallace extends Game {

  implicit lazy val renderContext = RenderContext()

  import renderContext.renderAssets._
  import renderContext.renderShortcuts._
  import renderContext.renderState._


  override def create(): Unit = {}

  override def render(): Unit = {

    glClearColor(0.5f, 0.5f, 0.5f, 0)
    glClear(GL_COLOR_BUFFER_BIT)

    val text = font20.prep(s"Fps: $fps", align = Align.center)

    batch {

      transform(_.scalexy(1.0f / 400.0f)) {

        batch.draw(libgdxLogo.texture, -libgdxLogo.width * 0.5f, -libgdxLogo.height / 2f)
        font20.draw(batch, text, 0f, 0f)

      }
    }

  }

  override def resize(w: Int, h: Int) {
    Projections.ortho11(renderContext.renderState.batch, w, h)
  }

  def fps = {
    Gdx.graphics.getFramesPerSecond
  }

}
