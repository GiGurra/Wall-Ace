package se.gigurra.wallace.client.renderer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20._
import se.gigurra.wallace.gamemodel.{TerrainStorage, World}

class Renderer {

  implicit val renderContext = RenderContext(new RenderAssets)

  import renderContext.assets
  import renderContext.drawShortcuts._
  import renderContext.glShortcuts._
  import renderContext.state._

  // End of constructor
  ///////////////////////

  def update[T <: TerrainStorage with RenderAsset](client_world: World[T]): Unit = {

    Projections.ortho11(Gdx.graphics.getWidth, Gdx.graphics.getHeight)
    glClearColor(0.0f, 0.0f, 0.0f, 0)
    glClear(GL_COLOR_BUFFER_BIT)

    batch {
      drawWorld(client_world)
      drawGui
    }

  }

  private def drawWorld[T <: TerrainStorage with RenderAsset](client_world: World[T]): Unit = {
    assets.maps.ensureHas("mapSprite", client_world.terrain.storage)
    val mapSprite = assets.maps("mapSprite")
    transform(_.unitSize(mapSprite).scalexy(renderContext.maxAspect).center(mapSprite)) {
      mapSprite.draw()
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
