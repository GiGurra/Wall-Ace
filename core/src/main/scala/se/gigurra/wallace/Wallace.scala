package se.gigurra.wallace

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20._
import se.gigurra.wallace.render._

object Map {
  val WIDTH = 1024
  val HEIGHT = 1024
}

class RenderAssets {
  val font20 = Font.fromTtfFile("fonts/pt-mono/PTM55FT.ttf", size = 40)
  val libgdxLogo = Sprite.fromFile("libgdxlogo.png", useMipMaps = false)
  val map = Sprite.fromSize(Map.WIDTH, Map.HEIGHT, useMipMaps = false)

  map.data.setColor(Color.BLACK)
  map.data.fill()
  map.upload()
}

class Wallace extends Game {

  implicit lazy val renderContext = RenderContext(new RenderAssets)

  import renderContext.assets._
  import renderContext.drawShortcuts._
  import renderContext.glShortcuts._
  import renderContext.state._

  override def create(): Unit = {}

  override def render(): Unit = {

    glClearColor(0.25f, 0.35f, 0.45f, 0)
    glClear(GL_COLOR_BUFFER_BIT)

    val text = prepText(font = font20, str = s"Fps: ${renderContext.fps}")

    map.upload()
    batch {

      transform(_.scalexy(1.0f / 400.0f)) {
        transform(_.center(libgdxLogo))(drawSprite(libgdxLogo))
        transform(_.center(text))(drawText(text))
      }

      val angle = System.nanoTime() / 1e9
      val mapX = math.cos(angle) * 0.5
      val mapY = math.sin(angle) * 0.5
      transform(_.translate(mapX.toFloat, mapY.toFloat)) {
        transform(_.unitSize(map).scalexy(0.1f).center(map))(drawSprite(map))
      }

    }

  }

  override def resize(w: Int, h: Int) {
    Projections.ortho11(w, h)
  }

}
