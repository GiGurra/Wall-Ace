package se.gigurra.wallace.client

import se.gigurra.wallace.client.clientstate.ClientStateManager
import se.gigurra.wallace.client.networkstate.NetworkStateManager
import se.gigurra.wallace.client.renderer._
import se.gigurra.wallace.client.worldstate.WorldStateManager

class Client(statCfg: StaticConfiguration,
             dynCfg: DynamicConfiguration) {

  import Renderables._
  import SpriteTerrainStoring._

  val networkStateMgr = NetworkStateManager(statCfg, dynCfg)
  val clientStateMgr = ClientStateManager(statCfg, dynCfg)
  val worldStateMgr = WorldStateManager(statCfg, dynCfg, SpriteTerrainStorageFactory)
  val renderer = Renderer(statCfg, dynCfg)

  object callbacks {

    // Also called just before dispose
    def onPause(): Unit = {

    }

    def onResume(): Unit = {

    }

    def onExit(): Unit = {

    }

    def onUpdate(): Unit = {
      // Super simple single threaded design:
      // update network
      // update world
      // update renderer
      networkStateMgr.update(clientStateMgr, worldStateMgr)
      clientStateMgr.update(networkStateMgr, worldStateMgr)
      worldStateMgr.update()
      renderer.update(clientStateMgr.state, worldStateMgr.state)
    }
  }
}
