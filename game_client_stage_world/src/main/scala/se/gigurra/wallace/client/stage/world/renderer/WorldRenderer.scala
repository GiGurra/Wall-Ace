package se.gigurra.wallace.client.stage.world.renderer

import com.badlogic.gdx.Gdx
import se.gigurra.wallace.client.stage.world.player.PlayerState
import se.gigurra.wallace.client.stage.world.renderer.elements._
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.gamemodel._

case class WorldRenderer(statCfg: StaticConfiguration,
                         dynCfg: DynamicConfiguration) {

  implicit val renderContext = RenderContext(RenderAssets())
  private val terrainRenderer = TerrainRenderer()
  private val entitiesRenderer = EntitiesRenderer()
  private val eventsRenderer = EventsRenderer()
  private val guiRenderer = WorldGuiRenderer()

  def update[T_TerrainStorage: Rendering](iSimFrame: WorldSimFrameIndex,
                                          clientState: PlayerState,
                                          worldState: World[T_TerrainStorage],
                                          worldEvents: Seq[WorldEvent]) = {

    renderContext.state.batch {

      Projections.ortho11(Gdx.graphics.getWidth, Gdx.graphics.getHeight)

      terrainRenderer.render(worldState.terrain)
      entitiesRenderer.render(worldState.allEntities)
      eventsRenderer.render(iSimFrame, worldEvents)
      guiRenderer.render(clientState, worldState)
    }
  }
}
