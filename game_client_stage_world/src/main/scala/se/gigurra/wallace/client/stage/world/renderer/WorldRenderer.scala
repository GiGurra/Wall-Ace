package se.gigurra.wallace.client.stage.world.renderer

import se.gigurra.wallace.client.stage.world.playerstate.PlayerState
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.gamemodel._
import se.gigurra.wallace.util.Time

case class WorldRenderer(statCfg: StaticConfiguration,
                         dynCfg: DynamicConfiguration) {

  import Renderables._

  implicit val renderContext = RenderContext(RenderAssets())

  import renderContext._

  private var events = Seq.empty[RenderEvent[WorldEvent]]

  ///////////////////////
  // Public API
  //

  def update[T_TerrainStorage: Rendering](iSimFrame: WorldSimFrameIndex,
                                          clientState: PlayerState,
                                          worldState: World[T_TerrainStorage],
                                          worldEvents: Seq[WorldEvent]) = frame2D {
    drawTerrain(worldState.terrain)
    drawEntities(worldState.entities())
    drawEvents(iSimFrame, worldEvents)
    drawWorldGui(clientState, worldState)
  }

  ///////////////////////
  // Helpers
  //

  private def drawTerrain[T_TerrainStorage: Rendering](terrain: Terrain[T_TerrainStorage]): Unit = {

    val mapSprite = assets.maps.getOrElseUpdate("mapSprite", terrain)

    transform(_
      .unitSize(mapSprite)
      .scalexy(maxAspect)
      .center(mapSprite)) {
      mapSprite.uploaded.draw()
    }
  }

  private def drawEntities(seq: Seq[Entity]) = {}

  private def drawEvents(iSimFrame: WorldSimFrameIndex, worldEvents: Seq[WorldEvent]) = {

    events ++= worldEvents.map(world2RenderEvent(iSimFrame, _))

    // TODO: Some drawing here..

    val t = Time.seconds
    events = events.filterNot(_.expired(t))
  }

  private def drawWorldGui(clientState: PlayerState, worldState: World[_]): Unit = {

    val gdxLogoAsset = assets.sprites.getOrElseUpdate("gdxLogo", assets.libgdxLogo)

    val text = prepText(font = assets.font20, str = s"Fps: ${renderContext.fps}")

    transform(_.scalexy(1.0f / 400.0f)) {
      transform(_.center(gdxLogoAsset))(gdxLogoAsset.draw())
      assets.temporary(text) foreach { text => transform(_.center(text))(text.draw()) }
    }
  }

}
