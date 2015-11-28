package se.gigurra.wallace.client.worldstate

import se.gigurra.wallace.config.client.DynamicConfiguration
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.gamemodel.{TerrainStorageFactory, TerrainStoring, World}

case class WorldStateManager[T_TerrainStorage: TerrainStoring](statCfg: StaticConfiguration,
                                                               dynCfg: DynamicConfiguration,
                                                               terrainStorageFactory: TerrainStorageFactory[T_TerrainStorage]) {

  private def time = System.currentTimeMillis()

  private var lastUpdateAt = time

  private def dt = statCfg.sim_dt

  val state = World.create(terrainStorageFactory, 640, 640)

  def update(): Unit = {
    while (lastUpdateAt + dt < time) {
      doUpdate()
      lastUpdateAt += dt
    }
  }

  private def doUpdate() = {
  }
}
