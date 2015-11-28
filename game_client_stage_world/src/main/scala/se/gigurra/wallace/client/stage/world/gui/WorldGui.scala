package se.gigurra.wallace.client.stage.world.gui

import se.gigurra.wallace.client.stage.world.player.PlayerState
import se.gigurra.wallace.client.stage.world.renderer.elements.WorldGuiRenderer
import se.gigurra.wallace.client.stage.world.renderer.{Sprite, RenderAssets, RenderContext}
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
             playerState: PlayerState,
             worldState: World[_]): Unit = {

    // TODO: Draw something here ..
    guiRenderer.render(playerState, worldState)
  }

}
