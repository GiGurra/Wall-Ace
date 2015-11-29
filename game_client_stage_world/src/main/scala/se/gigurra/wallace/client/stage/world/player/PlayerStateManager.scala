package se.gigurra.wallace.client.stage.world.player

import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.gamemodel.{WorldUpdate, WorldSimFrameIndex, WorldUpdateBatch}
import se.gigurra.wallace.input.InputEvent
import se.gigurra.wallace.stage.Stage

case class UpdatesFromPlayer(worldUpdates: Seq[WorldUpdate])

case class PlayerStateManager(statCfg: StaticConfiguration,
                              dynCfg: DynamicConfiguration) extends Stage[InputEvent] {

  override def stageId: String = "player-state-manager"

  val state = new PlayerState(ownUnitId = if (dynCfg.game_isSinglePlayer) Some("own-unit") else None)

  override def consumeInputs(inputs: Seq[InputEvent]): Seq[InputEvent] = {
    // TODO: Act on it... kind of
    inputs
  }

  def update(iSimFrame: WorldSimFrameIndex): UpdatesFromPlayer = {
    // TODO: Change network state somehow - e.g. send chat message perhaps ?
    UpdatesFromPlayer(Seq.empty)
  }


}
