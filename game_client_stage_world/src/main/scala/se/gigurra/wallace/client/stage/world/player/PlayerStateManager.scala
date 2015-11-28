package se.gigurra.wallace.client.stage.world.player

import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.gamemodel.{WorldUpdate, WorldSimFrameIndex, WorldUpdateBatch}

case class UpdatesFromPlayer(worldUpdates: Seq[WorldUpdate])

case class PlayerStateManager(statCfg: StaticConfiguration,
                              dynCfg: DynamicConfiguration) {

  val state = new PlayerState(ownUnitId = if (dynCfg.game_isSinglePlayer) Some("own-unit") else None)

  def update(iSimFrame: WorldSimFrameIndex): UpdatesFromPlayer = {
    // TODO: Change network state somehow - e.g. send chat message perhaps ?
    UpdatesFromPlayer(Seq.empty)
  }
}
