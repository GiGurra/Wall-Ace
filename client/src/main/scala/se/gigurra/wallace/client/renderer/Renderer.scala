package se.gigurra.wallace.client.renderer

import com.badlogic.gdx.{Gdx, Game}
import com.badlogic.gdx.graphics.GL20._
import se.gigurra.wallace.client.worldstate.Sector
import se.gigurra.wallace.gamemodel.World
import se.gigurra.wallace.gamemodel.worldGen.WorldGenerator

class Renderer {

  implicit val renderContext = RenderContext(new RenderAssets)

  import renderContext.assets
  import renderContext.drawShortcuts._
  import renderContext.glShortcuts._
  import renderContext.state._

  // End of constructor
  ///////////////////////

  def update(client_world: Option[Sector]): Unit = {

    Projections.ortho11(Gdx.graphics.getWidth, Gdx.graphics.getHeight)
    glClearColor(0.0f, 0.0f, 0.0f, 0)
    glClear(GL_COLOR_BUFFER_BIT)

    batch {
      client_world foreach drawWorld
      drawGui
    }

  }

  private def drawWorld(client_world: Sector): Unit = {
    assets.sprites.ensureHas("mapSprite", client_world.asset)
    val mapSprite = assets.sprites("mapSprite")
    transform(_.unitSize(mapSprite).scalexy(renderContext.maxAspect).center(mapSprite)) {
      drawSprite(mapSprite)
    }
  }

  private def drawGui(): Unit = {
    val text = prepText(font = assets.font20, str = s"Fps: ${renderContext.fps}")
    transform(_.scalexy(1.0f / 400.0f)) {
      transform(_.center(assets.libgdxLogo))(drawSprite(assets.libgdxLogo))
      transform(_.center(text))(drawText(text))
    }
  }

}
