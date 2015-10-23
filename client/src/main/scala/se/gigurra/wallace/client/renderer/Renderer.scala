package se.gigurra.wallace.client.renderer

import com.badlogic.gdx.{Gdx, Game}
import com.badlogic.gdx.graphics.GL20._
import se.gigurra.wallace.gamemodel.World
import se.gigurra.wallace.gamemodel.worldGen.WorldGenerator

class Renderer {

  implicit val renderContext = RenderContext(new RenderAssets)

  import renderContext.assets._
  import renderContext.drawShortcuts._
  import renderContext.glShortcuts._
  import renderContext.state._

  val world = World(renderContext.assets.mapSprite.pixels, renderContext.assets.mapSprite.width, renderContext.assets.mapSprite.height)
  WorldGenerator.generate("MyMapSeed", world)
  mapSprite.upload()

  // End of constructor
  ///////////////////////

  def update(): Unit = {

    Projections.ortho11(Gdx.graphics.getWidth, Gdx.graphics.getHeight)
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

}
