package se.gigurra.wallace.client.clientstate

import se.gigurra.wallace.WorldVector
import se.gigurra.wallace.client.{DynamicConfiguration, StaticConfiguration}

class ClientStateManager(statCfg: StaticConfiguration,
                         dynCfg: DynamicConfiguration) {

  var menuOpen = false
  var camera = new Camera(worldPosition = WorldVector())

  def update(): Unit = {
    val dt = statCfg.sim_dt

  }
}
