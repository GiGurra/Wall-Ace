package se.gigurra.wallace.client.renderer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20._
import se.gigurra.wallace.client.clientstate.ClientState
import se.gigurra.wallace.config.client.DynamicConfiguration
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.gamemodel.{Terrain, World}

case class GameRenderer(statCfg: StaticConfiguration,
                        dynCfg: DynamicConfiguration) {

  import Renderables._

  implicit val renderContext = RenderContext(new RenderAssets)

  import renderContext._
  import renderContext.glShortcuts._
  import renderContext.state._

  // End of constructor
  ///////////////////////

  def update[T_TerrainStorage: Rendering](clientState: ClientState,
                                          worldState: World[T_TerrainStorage]) = Frame2D {
    drawTerrain(worldState.terrain)
    drawGui
  }

  private def Frame2D(impl: => Unit): Unit = {
    batch {
      Projections.ortho11(Gdx.graphics.getWidth, Gdx.graphics.getHeight)
      glClearColor(0.0f, 0.0f, 0.0f, 0)
      glClear(GL_COLOR_BUFFER_BIT)
      impl
    }
  }

  private def drawTerrain[T_TerrainStorage: Rendering](terrain: Terrain[T_TerrainStorage]): Unit = {

    val mapSprite = assets.maps.getOrElseUpdate("mapSprite", terrain)

    transform(_
      .unitSize(mapSprite)
      .scalexy(maxAspect)
      .center(mapSprite)) {
      mapSprite.uploaded.draw()
    }
  }

  private def drawGui(): Unit = {

    val gdxLogoAsset = assets.sprites.getOrElseUpdate("gdxLogo", assets.libgdxLogo)

    val text = prepText(font = assets.font20, str = s"Fps: ${renderContext.fps}")

    transform(_.scalexy(1.0f / 400.0f)) {
      transform(_.center(gdxLogoAsset))(gdxLogoAsset.draw())
      assets.temporary(text) foreach { text => transform(_.center(text))(text.draw()) }
    }
  }

}
