package se.gigurra.wallace.client.stage.world.networkstate

import se.gigurra.wallace.client.stage.world.playerstate.UpdatesFromPlayer
import se.gigurra.wallace.gamemodel.{TerrainStoring, WorldSimFrameIndex, WorldUpdateBatch}

case class UpdatesFromNetwork(worldUpdates: Seq[WorldUpdateBatch])

case class NetworkStateManager(isSinglePlayer: Boolean) {

  def update[T_TerrainStorage: TerrainStoring](iSimFrame: WorldSimFrameIndex,
                                              playerInput: UpdatesFromPlayer): UpdatesFromNetwork = {

    // Short curcuit if single player!
    if (isSinglePlayer)
      return UpdatesFromNetwork(worldUpdates = Seq(playerInput.worldUpdates))

    ???
  }

  def close(): Unit = {
  }

}
