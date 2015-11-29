package se.gigurra.wallace.client.stage.world.player

import se.gigurra.wallace.client.stage.world.renderer.WorldRenderer
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.gamemodel.{WorldUpdate, WorldSimFrameIndex}
import se.gigurra.wallace.input.{MousePositionUpdate, InputEvent}
import se.gigurra.wallace.stage.Stage

case class UpdatesFromPlayer(worldUpdates: Seq[WorldUpdate])

case class PlayerStateManager(statCfg: StaticConfiguration,
                              dynCfg: DynamicConfiguration) extends Stage[InputEvent] {

  override def stageId: String = "player-state-manager"

  val state = new PlayerState(ownUnitId = if (dynCfg.game_isSinglePlayer) Some("own-unit") else None)

  override def consumeInput(input: InputEvent): Option[InputEvent] = {

    // TODO: Act on it... kind of
    input match {
      case MousePositionUpdate(_, position) =>
        val mouseWorldPos = WorldRenderer.pixelPos2WorldPos(position, state.camera)
        // val pixelPosBack = WorldRenderer.worldPos2PixelPos(mouseWorldPos, state.camera)
        // println(s"mousePixelPos / pixelPosBack / mouseWorldPos: ${position} / ${pixelPosBack} / ${mouseWorldPos}")
        state.cursorWorldPosition = mouseWorldPos
      case _ =>
    }

    Some(input)
  }

  def update(iSimFrame: WorldSimFrameIndex): UpdatesFromPlayer = {
    // TODO: Change network state somehow - e.g. send chat message perhaps ?
    UpdatesFromPlayer(Seq.empty)
  }

}
