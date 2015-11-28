package se.gigurra.wallace.client.stage.world

import se.gigurra.wallace.client.stage.world.audio.AudioStateManager
import se.gigurra.wallace.client.stage.world.player.PlayerStateManager
import se.gigurra.wallace.client.stage.world.network.NetworkStateManager
import se.gigurra.wallace.client.stage.world.renderer.elements.Renderables
import se.gigurra.wallace.client.stage.world.renderer.terrainstorage.{SpriteTerrainStorageFactory, SpriteTerrainStoring}
import se.gigurra.wallace.client.stage.world.renderer.WorldRenderer
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.gamemodel.{WorldSimFrameIndex, WorldStateManager}
import se.gigurra.wallace.input.InputEvent
import se.gigurra.wallace.stage.{Stage, StageManager}

class WorldStage(statCfg: StaticConfiguration,
                 dynCfg: DynamicConfiguration,
                 stageManager: StageManager) extends Stage {

  override def stageId: String = "game-simulation"

  import Renderables._
  import SpriteTerrainStoring._

  val playerStateMgr = PlayerStateManager(statCfg, dynCfg)

  val networkStateMgr = NetworkStateManager(isSinglePlayer)

  val worldStateMgr = WorldStateManager(
    SpriteTerrainStorageFactory,
    statCfg.sim_dt,
    dynCfg.game_isSinglePlayer)

  val audioStateMgr = AudioStateManager(statCfg, dynCfg)

  val renderer = WorldRenderer(statCfg, dynCfg)

  //////////////////////////
  // Callbacks

  override def consumeInputs(inputs: Seq[InputEvent]): Seq[InputEvent] = {
    inputs
  }

  override def update(): Unit = {
    val updatesFromPlayer = playerStateMgr.update(iSimFrame)
    val updatesFromNetwork = networkStateMgr.update(iSimFrame, updatesFromPlayer)
    val worldEvents = worldStateMgr.update(updatesFromNetwork.worldUpdates)
    audioStateMgr.update(worldEvents)
    renderer.update(iSimFrame, playerStateMgr.state, worldStateMgr.state, worldEvents)
  }

  override def onClose(): Unit = {
    networkStateMgr.close()
  }

  def isSinglePlayer: Boolean = dynCfg.game_isSinglePlayer

  def iSimFrame: WorldSimFrameIndex = worldStateMgr.iSimFrame

}
