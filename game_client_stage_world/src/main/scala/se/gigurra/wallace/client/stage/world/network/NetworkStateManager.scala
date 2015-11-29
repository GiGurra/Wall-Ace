package se.gigurra.wallace.client.stage.world.network

import java.io.Closeable

import se.gigurra.wallace.client.stage.world.LocalUpdates
import se.gigurra.wallace.gamemodel.{TerrainStoring, WorldSimFrameIndex, WorldUpdateBatch}

case class UpdatesFromNetwork(worldUpdates: Seq[WorldUpdateBatch])

case class UpdateToNetwork(updates: LocalUpdates)

case class NetworkStateManager(isSinglePlayer: Boolean) extends Closeable {

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
