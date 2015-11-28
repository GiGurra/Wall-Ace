package se.gigurra.wallace.client.stage.world.network

import se.gigurra.wallace.client.stage.world.LocalUpdates
import se.gigurra.wallace.gamemodel.{WorldUpdate, TerrainStoring, WorldSimFrameIndex, WorldUpdateBatch}

case class UpdatesFromNetwork(worldUpdates: Seq[WorldUpdateBatch])

case class UpdateToNetwork(updates: LocalUpdates)

case class NetworkStateManager(isSinglePlayer: Boolean) {

  def update[T_TerrainStorage: TerrainStoring](iSimFrame: WorldSimFrameIndex,
                                              input: UpdateToNetwork): UpdatesFromNetwork = {

    // Short curcuit if single player!
    if (isSinglePlayer)
      return UpdatesFromNetwork(worldUpdates = Seq(WorldUpdateBatch(iSimFrame, input.updates.worldUpdates)))

    ???
  }

  def close(): Unit = {
  }

}
