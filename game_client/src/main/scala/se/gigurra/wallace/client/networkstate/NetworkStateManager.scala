package se.gigurra.wallace.client.networkstate

import se.gigurra.wallace.client.clientstate.ClientStateManager
import se.gigurra.wallace.client.worldstate.WorldStateManager
import se.gigurra.wallace.config.client.DynamicConfiguration
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.gamemodel.TerrainStoring

case class NetworkStateManager(statCfg: StaticConfiguration,
                               dynCfg: DynamicConfiguration) {

  def update[T_TerrainStorage: TerrainStoring](clientStateMgr: ClientStateManager,
                                               worldStateMgr: WorldStateManager[T_TerrainStorage]): Unit = {

  }

  def close(): Unit = {
    
  }

}
