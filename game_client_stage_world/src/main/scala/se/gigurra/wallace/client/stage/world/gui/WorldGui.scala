package se.gigurra.wallace.client.stage.world.gui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import se.gigurra.wallace.client.stage.world.player.PlayerState
import se.gigurra.wallace.client.stage.world.renderer.elements.WorldGuiRenderer
import se.gigurra.wallace.client.stage.world.renderer.WorldRenderer
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.cursors.HardwareCursor
import se.gigurra.wallace.gamemodel.{World, WorldSimFrameIndex, WorldUpdate}
import se.gigurra.wallace.input.{MousePositionUpdate, InputEvent}
import se.gigurra.wallace.renderer.{RenderContext, RenderAssets}
import se.gigurra.wallace.stage.Stage
import se.gigurra.wallace.util.{Vec2FixedPoint, SyncQue}

case class UpdatesFromGui(worldUpdates: Seq[WorldUpdate])

case class WorldGui(statCfg: StaticConfiguration,
                    dynCfg: DynamicConfiguration)
                   (implicit renderContext: RenderContext[RenderAssets])
  extends Stage[InputEvent] {

  override def stageId: String = "world-gui"

  private val guiRenderer = WorldGuiRenderer()
  private val queuedWorldUpdates = SyncQue[WorldUpdate]

  def handleMouseOnTopOfGui(input: MousePositionUpdate,
                            position: Vec2FixedPoint): Option[InputEvent] = {

    val offsFromBottom = (Gdx.graphics.getHeight - position.y).toFloat / Gdx.graphics.getHeight.toFloat

    if (offsFromBottom < 0.15f) {
      HardwareCursor.standard.set()
      None
    } else {
      Some(input)
    }

  }

  override def consumeInput(input: InputEvent): Option[InputEvent] = {

    // TODO: Act on it... kind of
    input match {
      case input @ MousePositionUpdate(dragged @ false, position) =>
        handleMouseOnTopOfGui(input, position)
      case _ =>
        Some(input)
    }
  }

  def popQueued: UpdatesFromGui = UpdatesFromGui(queuedWorldUpdates.pop())

  def update(iSimFrame: WorldSimFrameIndex,
             player: PlayerState,
             world: World[_]): Unit = {

    import renderContext._

    import se.gigurra.wallace.client.stage.world.renderer._

    // TODO: Draw something here ..
    guiRenderer.render(player, world)


  }

}
