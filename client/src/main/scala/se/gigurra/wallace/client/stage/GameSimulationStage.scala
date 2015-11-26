package se.gigurra.wallace.client.stage

import se.gigurra.wallace.client.clientstate.ClientStateManager
import se.gigurra.wallace.client.renderer.{SpriteTerrainStoring, Renderables, GameRenderer, SpriteTerrainStorageFactory}
import se.gigurra.wallace.client.toplevelmanagers.StageManager
import se.gigurra.wallace.client.worldstate.WorldStateManager
import se.gigurra.wallace.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.client.networkstate.NetworkStateManager
import se.gigurra.wallace.input.InputEvent

class GameSimulationStage(statCfg: StaticConfiguration,
                          dynCfg: DynamicConfiguration,
                          stageManager: StageManager) extends Stage {

  override def stageId: String = "game-simulation"

  import Renderables._
  import SpriteTerrainStoring._

  val networkStateMgr = NetworkStateManager(statCfg, dynCfg)
  val clientStateMgr = ClientStateManager(statCfg, dynCfg)
  val worldStateMgr = WorldStateManager(statCfg, dynCfg, SpriteTerrainStorageFactory)
  val renderer = GameRenderer(statCfg, dynCfg)

  override def consumeInputs(inputs: Seq[InputEvent]): Seq[InputEvent] = {
    inputs
  }

  override def update(): Unit = {
    // TODO: Draw stuff... Run game sim.. etc .. or?? Network connections perhaps?

    // Super simple single threaded design:
    // update network
    // update world
    // update renderer
    networkStateMgr.update(clientStateMgr, worldStateMgr)
    clientStateMgr.update(networkStateMgr, worldStateMgr)
    worldStateMgr.update()
    renderer.update(clientStateMgr.state, worldStateMgr.state)
  }


  override def onClose(): Unit = {
    networkStateMgr.close()
  }

  override def onOpen(): Unit = {

  }

}
