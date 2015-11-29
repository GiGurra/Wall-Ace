package se.gigurra.wallace.client.stage.world.gui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import se.gigurra.wallace.client.stage.world.player.PlayerState
import se.gigurra.wallace.client.stage.world.renderer.elements.WorldGuiRenderer
import se.gigurra.wallace.client.stage.world.renderer.{RenderAssets, RenderContext}
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.gamemodel.{World, WorldSimFrameIndex, WorldUpdate}
import se.gigurra.wallace.input.InputEvent
import se.gigurra.wallace.stage.Stage
import se.gigurra.wallace.util.SyncQue

case class UpdatesFromGui(worldUpdates: Seq[WorldUpdate])

case class WorldGui(statCfg: StaticConfiguration,
                    dynCfg: DynamicConfiguration)
                   (implicit renderContext: RenderContext[RenderAssets])
  extends Stage[InputEvent] {

  override def stageId: String = "world-gui"

  private val guiRenderer = WorldGuiRenderer()
  private val queuedWorldUpdates = SyncQue[WorldUpdate]

  override def consumeInputs(inputs: Seq[InputEvent]): Seq[InputEvent] = {
    inputs
  }

  def popQueued: UpdatesFromGui = UpdatesFromGui(queuedWorldUpdates.pop())

  def update(iSimFrame: WorldSimFrameIndex,
             player: PlayerState,
             world: World[_]): Unit = {

    import renderContext._


    val greenClear = new Color(0, 1, 0, 0)
    batch {
      transform(_.scalexy(0.25f)) {
        drawShapes(ShapeType.Filled) { sr =>
          sr.rect(-0.5f, -0.5f, 1.0f, 1.0f, Color.RED, greenClear, greenClear, Color.RED)
        }
      }
    }

    // TODO: Draw something here ..
    guiRenderer.render(player, world)
  }

}
