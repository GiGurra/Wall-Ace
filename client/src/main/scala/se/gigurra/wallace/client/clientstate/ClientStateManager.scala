package se.gigurra.wallace.client.clientstate

import se.gigurra.wallace.client.networkstate.NetworkStateManager
import se.gigurra.wallace.client.worldstate.WorldStateManager
import se.gigurra.wallace.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.gamemodel.TerrainStoring

case class ClientStateManager(statCfg: StaticConfiguration,
                              dynCfg: DynamicConfiguration) {

  val state = new ClientState

  def update[T_TerrainStorage: TerrainStoring](networkStateMgr: NetworkStateManager,
                                               worldStateMgr: WorldStateManager[T_TerrainStorage]) = {

  }
}
