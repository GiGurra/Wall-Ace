package se.gigurra.wallace.client.stage.world

import se.gigurra.wallace.client.stage.world.playerstate.PlayerStateManager
import se.gigurra.wallace.client.stage.world.networkstate.NetworkStateManager
import se.gigurra.wallace.client.stage.world.renderer.{WorldRenderer, SpriteTerrainStorageFactory, SpriteTerrainStoring}
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.gamemodel.{WorldSimFrameIndex, WorldStateManager}
import se.gigurra.wallace.input.InputEvent
import se.gigurra.wallace.stage.{Stage, StageManager}
import se.gigurra.wallace.client.stage.world.renderer.Renderables

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
    renderer.update(iSimFrame, playerStateMgr.state, worldStateMgr.state, worldEvents)
  }

  override def onClose(): Unit = {
    networkStateMgr.close()
  }

  def isSinglePlayer: Boolean = dynCfg.game_isSinglePlayer

  def iSimFrame: WorldSimFrameIndex = worldStateMgr.iSimFrame

}
