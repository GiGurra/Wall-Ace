package se.gigurra.wallace.client.stage.world

import com.badlogic.gdx.Gdx
import se.gigurra.wallace.client.stage.world.audio.AudioStateManager
import se.gigurra.wallace.client.stage.world.gui.WorldGui
import se.gigurra.wallace.client.stage.world.player.{PlayerState, PlayerStateManager}
import se.gigurra.wallace.client.stage.world.network.{UpdateToNetwork, NetworkStateManager}
import se.gigurra.wallace.client.stage.world.renderer.elements.Renderables
import se.gigurra.wallace.client.stage.world.renderer.terrainstorage.{SpriteTerrainStorageFactory, SpriteTerrainStoring}
import se.gigurra.wallace.client.stage.world.renderer._
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.gamemodel._
import se.gigurra.wallace.input.InputEvent
import se.gigurra.wallace.stage.{Stage, StageManager}

class WorldStage(statCfg: StaticConfiguration,
                 dynCfg: DynamicConfiguration,
                 stageManager: StageManager[InputEvent]) extends Stage[InputEvent] {

  override def stageId: String = "game-simulation"

  import Renderables._
  import SpriteTerrainStoring._

  //////////////////////////
  // States
  //

  val playerStateMgr = PlayerStateManager(statCfg, dynCfg)

  val networkStateMgr = NetworkStateManager(isSinglePlayer)

  val worldStateMgr = WorldStateManager(
    SpriteTerrainStorageFactory,
    statCfg.sim_dt,
    dynCfg.game_isSinglePlayer)

  val audioStateMgr = AudioStateManager(statCfg, dynCfg)

  val worldRenderContext = RenderContext(RenderAssets())
  val worldRenderer = WorldRenderer(statCfg, dynCfg)(worldRenderContext)
  val worldGui = WorldGui(statCfg, dynCfg)(worldRenderContext)


  //////////////////////////
  // Callbacks
  //

  override def consumeInputs(inputs: Seq[InputEvent]): Seq[InputEvent] = {
    val inputConsumers = Seq(worldGui, playerStateMgr)
    val inputsLeft = inputConsumers.foldLeft(inputs)((inputs, item) => item.consumeInputs(inputs))
    inputsLeft
  }

  override def update(): Unit = {
    val worldEvents = simulate(iSimFrame)
    playAudio(iSimFrame, worldEvents)
    render(iSimFrame, playerStateMgr.state, worldStateMgr.state, worldEvents)
  }

  override def onClose(): Unit = {
    networkStateMgr.close()
    audioStateMgr.close()
    worldRenderContext.close()
  }

  //////////////////////////
  // Helpers
  //

  private def simulate(iSimFrame: WorldSimFrameIndex): Seq[WorldEvent] = {
    val localUpdates = getLocalUpdates(iSimFrame)
    val updates = networkStateMgr.update(iSimFrame, UpdateToNetwork(localUpdates))
    worldStateMgr.update(updates.worldUpdates)
  }

  private def getLocalUpdates(iSimFrame: WorldSimFrameIndex): LocalUpdates = {
    val updatesFromPlayer = playerStateMgr.update(iSimFrame)
    val updatesFromGui = worldGui.popQueued
    val rawWorldUpdates = updatesFromPlayer.worldUpdates ++ updatesFromGui.worldUpdates
    LocalUpdates(rawWorldUpdates)
  }

  private def playAudio(iSimFrame: WorldSimFrameIndex,
                        worldEvents: Seq[WorldEvent]): Unit = {
    audioStateMgr.update(iSimFrame, worldEvents)
  }

  private def render(iSimFrame: WorldSimFrameIndex,
                     playerState: PlayerState,
                     worldState: World[Sprite],
                     worldEvents: Seq[WorldEvent]): Unit = {

    implicit val _wrctx = worldRenderContext

    worldRenderContext.state.batch {
      Projections.ortho11(Gdx.graphics.getWidth, Gdx.graphics.getHeight)
      worldRenderer.update(iSimFrame, playerStateMgr.state, worldStateMgr.state, worldEvents)
      worldGui.update(iSimFrame, playerStateMgr.state, worldStateMgr.state)
    }
  }


  private def isSinglePlayer: Boolean = dynCfg.game_isSinglePlayer

  private def iSimFrame: WorldSimFrameIndex = worldStateMgr.iSimFrame

}

case class LocalUpdates(worldUpdates: Seq[WorldUpdate])
