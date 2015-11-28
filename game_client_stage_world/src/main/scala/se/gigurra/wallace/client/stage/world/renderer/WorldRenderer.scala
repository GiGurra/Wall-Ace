package se.gigurra.wallace.client.stage.world.renderer

import se.gigurra.wallace.client.stage.world.playerstate.PlayerState
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.gamemodel.{WorldSimFrameIndex, WorldEvent, Terrain, World}
import se.gigurra.wallace.util.Time

case class WorldRenderer(statCfg: StaticConfiguration,
                         dynCfg: DynamicConfiguration) {

  import Renderables._

  implicit val renderContext = RenderContext(new RenderAssets)
  import renderContext._

  private var events: Seq[RenderEvent[WorldEvent]] = Seq.empty

  // End of constructor
  ///////////////////////

  def update[T_TerrainStorage: Rendering](iSimFrame: WorldSimFrameIndex,
                                          clientState: PlayerState,
                                          worldState: World[T_TerrainStorage],
                                          worldEvents: Seq[WorldEvent]) = frame2D {
    addEvents(iSimFrame, worldEvents)
    drawTerrain(worldState.terrain)
    drawEvents()
    drawWorldGui()
    removeExpiredEvents()
  }

  private def addEvents(iSimFrame: WorldSimFrameIndex, worldEvents: Seq[WorldEvent]): Unit = {
    events ++= worldEvents.map(world2RenderEvent(iSimFrame, _))
  }

  private def removeExpiredEvents(): Unit = {
    val t = Time.seconds
    events = events.filterNot(_.expired(t))
  }

  private def drawEvents(): Unit = {}

  private def drawTerrain[T_TerrainStorage: Rendering](terrain: Terrain[T_TerrainStorage]): Unit = {

    val mapSprite = assets.maps.getOrElseUpdate("mapSprite", terrain)

    transform(_
      .unitSize(mapSprite)
      .scalexy(maxAspect)
      .center(mapSprite)) {
      mapSprite.uploaded.draw()
    }
  }

  private def drawWorldGui(): Unit = {

    val gdxLogoAsset = assets.sprites.getOrElseUpdate("gdxLogo", assets.libgdxLogo)

    val text = prepText(font = assets.font20, str = s"Fps: ${renderContext.fps}")

    transform(_.scalexy(1.0f / 400.0f)) {
      transform(_.center(gdxLogoAsset))(gdxLogoAsset.draw())
      assets.temporary(text) foreach { text => transform(_.center(text))(text.draw()) }
    }
  }

}
