package se.gigurra.wallace.client.stage.world.renderer

import se.gigurra.wallace.client.stage.world.player.PlayerState
import se.gigurra.wallace.client.stage.world.renderer.elements._
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.gamemodel._

case class WorldRenderer(statCfg: StaticConfiguration,
                         dynCfg: DynamicConfiguration)
                        (implicit renderContext: RenderContext[RenderAssets]) {

  private val terrainRenderer = TerrainRenderer()
  private val entitiesRenderer = EntitiesRenderer()
  private val eventsRenderer = EventsRenderer()

  def update[T_TerrainStorage: Rendering](iSimFrame: WorldSimFrameIndex,
                                          clientState: PlayerState,
                                          worldState: World[T_TerrainStorage],
                                          worldEvents: Seq[WorldEvent]) = {

    terrainRenderer.render(worldState.terrain)
    entitiesRenderer.render(worldState.allEntities)
    eventsRenderer.render(iSimFrame, worldEvents)
  }
}
