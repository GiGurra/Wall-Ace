package se.gigurra.wallace.client.stage.world

import se.gigurra.wallace.client.stage.world.clientstate.ClientStateManager
import se.gigurra.wallace.client.stage.world.networkstate.NetworkStateManager
import se.gigurra.wallace.client.stage.world.renderer.{GameRenderer, SpriteTerrainStorageFactory, SpriteTerrainStoring}
import se.gigurra.wallace.client.stage.world.worldstate.WorldStateManager
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.input.InputEvent
import se.gigurra.wallace.stage.{Stage, StageManager}
import se.gigurra.wallace.client.stage.world.renderer.Renderables

class WorldStage(statCfg: StaticConfiguration,
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
