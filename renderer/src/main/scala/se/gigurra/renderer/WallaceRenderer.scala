package se.gigurra.renderer

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.GL20._
import se.gigurra.wallace.model.World
import se.gigurra.wallace.model.worldGen.WorldGenerator

class RenderAssets {
  val font20 = Font.fromTtfFile("fonts/pt-mono/PTM55FT.ttf", size = 40)
  val libgdxLogo = Sprite.fromFile("libgdxlogo.png", useMipMaps = false)
  val mapSprite = Sprite.fromSize(256, 256, useMipMaps = true)
}

class Wallace extends Game {

  implicit lazy val renderContext = RenderContext(new RenderAssets)
  implicit lazy val world = World(renderContext.assets.mapSprite.pixels, renderContext.assets.mapSprite.width, renderContext.assets.mapSprite.height)

  import renderContext.assets._
  import renderContext.drawShortcuts._
  import renderContext.glShortcuts._
  import renderContext.state._

  override def create(): Unit = {
    WorldGenerator.generate("MyMapSeed", world)
    mapSprite.upload()
  }

  override def render(): Unit = {

    glClearColor(0.0f, 0.0f, 0.0f, 0)
    glClear(GL_COLOR_BUFFER_BIT)

    val text = prepText(font = font20, str = s"Fps: ${renderContext.fps}")

    batch {

      transform(_.unitSize(mapSprite).scalexy(renderContext.maxAspect).center(mapSprite)) {
        drawSprite(mapSprite)
      }

      transform(_.scalexy(1.0f / 400.0f)) {
        transform(_.center(libgdxLogo))(drawSprite(libgdxLogo))
        transform(_.center(text))(drawText(text))
      }
    }

  }

  override def resize(w: Int, h: Int) {
    Projections.ortho11(w, h)
  }

}
