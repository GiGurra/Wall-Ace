package se.gigurra.wallace.client.stage.world.clientstate

import se.gigurra.wallace.client.stage.world.networkstate.NetworkStateManager
import se.gigurra.wallace.client.stage.world.worldstate.WorldStateManager
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.gamemodel.TerrainStoring

case class ClientStateManager(statCfg: StaticConfiguration,
                              dynCfg: DynamicConfiguration) {

  val state = new ClientState

  def update[T_TerrainStorage: TerrainStoring](networkStateMgr: NetworkStateManager,
                                               worldStateMgr: WorldStateManager[T_TerrainStorage]) = {

  }
}
