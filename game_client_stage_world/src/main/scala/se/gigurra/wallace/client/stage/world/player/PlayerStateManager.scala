package se.gigurra.wallace.client.stage.world.player

import java.util.UUID

import com.badlogic.gdx.Input.Keys._
import se.gigurra.wallace.client.stage.world.renderer.WorldRenderer
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.gamemodel._
import se.gigurra.wallace.input.{KeyUpdate, MousePositionUpdate, InputEvent}
import se.gigurra.wallace.input.InputArithmetics._
import se.gigurra.wallace.stage.Stage
import se.gigurra.wallace.util.SyncQue

case class UpdatesFromPlayer(worldUpdates: Seq[WorldUpdate])

case class PlayerStateManager(statCfg: StaticConfiguration,
                              dynCfg: DynamicConfiguration) extends Stage[InputEvent] {


  ///////////////////////
  //  Members
  //

  override val stageId: String = "player-state-manager"
  private val queuedFromConsumedInput = SyncQue[WorldUpdate]()
  val state = new PlayerState(statCfg.sim_patch2WorldScale)


  ///////////////////////
  //  API
  //

  override def consumeInput(input: InputEvent): Option[InputEvent] = {

    // TODO: Act on it... kind of
    input match {
      case MousePositionUpdate(_, position) =>
        val mouseWorldPos = WorldRenderer.pixelPos2WorldPos(position, state.camera)
        // val pixelPosBack = WorldRenderer.worldPos2PixelPos(mouseWorldPos, state.camera)
        // println(s"mousePixelPos / pixelPosBack / mouseWorldPos: ${position} / ${pixelPosBack} / ${mouseWorldPos}")
        state.cursorWorldPosition = mouseWorldPos
      case KeyUpdate(_, key) if Seq(W, S, A, D).contains(key) =>
        updateOwnCharacterMovement()
      case _ =>
    }

    Some(input)
  }

  def update(iSimFrame: WorldSimFrameIndex, ownUnitPos: Option[WorldVector]): UpdatesFromPlayer = {

    // Update camera to follow own unit
    for {
      id <- state.unitId
      ownUnitPos <- ownUnitPos
    } {
      state.camera.worldPosition = ownUnitPos
    }

    UpdatesFromPlayer(queuedFromConsumedInput.pop())
  }


  ///////////////////////
  //  Helpers
  //

  private def m2WorldScale: Int = World.m2World(state.patch2WorldScale)

  private def getOwnMoveVelocity(): WorldVector = {

    val maxSpeedMps = 3
    val maxSpeedWorld = maxSpeedMps * m2WorldScale

    val w = WorldVector(0, 1) * W.keyDown
    val s = WorldVector(0, -1) * S.keyDown
    val a = WorldVector(-1, 0) * A.keyDown
    val d = WorldVector(1, 0) * D.keyDown

    (w + s + a + d)
      .normalized(
        newLength = maxSpeedWorld,
        acceptZeroLength = true)
  }

  private def updateOwnCharacterMovement(): Unit = {

    for (id <- state.unitId) {

      val moveVelocity = getOwnMoveVelocity()

      queuedFromConsumedInput.add(new WorldUpdate {
        override def apply(world: World[_])(implicit eventReceiver: WorldEventReceiver): Unit = {
          world.getEntity(id) match {
            case Some(ownEntity) => ownEntity.velocity = Some(moveVelocity)
            case None =>
          }
        }
      })
    }
  }

}
