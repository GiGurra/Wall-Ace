package se.gigurra.wallace.client.stage.world.renderer

import se.gigurra.wallace.client.stage.world.clientstate.ClientState
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.gamemodel.{Terrain, World}

case class GameRenderer(statCfg: StaticConfiguration,
                        dynCfg: DynamicConfiguration) extends Frame2DRender {

  import Renderables._

  implicit val renderContext = RenderContext(new RenderAssets)
  import renderContext._

  // End of constructor
  ///////////////////////

  def update[T_TerrainStorage: Rendering](clientState: ClientState,
                                          worldState: World[T_TerrainStorage]) = frame2D {
    drawTerrain(worldState.terrain)
    drawGui
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
