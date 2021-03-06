package se.gigurra.wallace.client.stage.world.renderer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import se.gigurra.wallace.client.stage.world.player.{Camera, PlayerState}
import se.gigurra.wallace.renderer.{Rendering, RenderContext, RenderAssets, Matrix4Stack}
import Matrix4Stack.Matrix4Stack
import se.gigurra.wallace.client.stage.world.renderer.elements._
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.cursors.HardwareCursor
import se.gigurra.wallace.gamemodel._
import se.gigurra.wallace.input.{MousePositionUpdate, InputEvent}
import se.gigurra.wallace.util.Vec2FixedPoint

case class WorldRenderer(statCfg: StaticConfiguration,
                         dynCfg: DynamicConfiguration)
                        (implicit renderContext: RenderContext[RenderAssets]) {

  import WorldRenderer._

  private val terrainRenderer = TerrainRenderer()
  private val entitiesRenderer = EntitiesRenderer()
  private val eventsRenderer = EventsRenderer()

  def consumeInput(remainingInput: InputEvent): Option[InputEvent] = {
    remainingInput match {
      case MousePositionUpdate(_,_) => HardwareCursor.aim.set()
      case _ =>
    }
    None
  }

  def update[T_TerrainStorage: Rendering](iSimFrame: WorldSimFrameIndex,
                                          player: PlayerState,
                                          worldState: World[T_TerrainStorage],
                                          worldEvents: Seq[WorldEvent]) = {

    import renderContext._
    import renderContext.glShortcuts._

    batch {
      transform(toWorldSpace(_, player.camera)) {

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        glClear(GL20.GL_COLOR_BUFFER_BIT)

        terrainRenderer.render(worldState.terrain)
        entitiesRenderer.render(worldState.allEntities)
        eventsRenderer.render(iSimFrame, worldEvents)
      }
    }
  }

}

object WorldRenderer {

  def toWorldSpace(stack: Matrix4Stack, camera: Camera): Matrix4Stack = {
    stack
      .scalexy(1.0f / camera.zoom_distUnitPerScreenUnit)
      .translate(-camera.worldPosition)
  }

  def pixelPos2WorldPos(mousePos: Vec2FixedPoint,
                        camera: Camera): WorldVector = {

    val xCtrPixels: Float = 0.5f * Gdx.graphics.getWidth.toFloat
    val yCtrPixels: Float = 0.5f * Gdx.graphics.getHeight.toFloat

    val halfWidthPixels = xCtrPixels
    val halfHeightPixels = yCtrPixels
    val halfDimMin = math.min(halfWidthPixels, halfHeightPixels)

    val xOffsPixels: Float = mousePos.x.toFloat - xCtrPixels
    val yOffsPixels: Float = mousePos.y.toFloat - yCtrPixels

    val xOffsRel = xOffsPixels / halfDimMin
    val yOffsRel = yOffsPixels / halfDimMin

    camera.worldPosition + WorldVector(
      math.round(camera.zoom_distUnitPerScreenUnit * xOffsRel),
      -math.round(camera.zoom_distUnitPerScreenUnit * yOffsRel)
    )
  }


  def worldPos2PixelPos(worldPos: WorldVector,
                        camera: Camera): Vec2FixedPoint = {

    val widthPixels = Gdx.graphics.getWidth
    val heightPixels = Gdx.graphics.getHeight

    val xCtrPixels: Float = 0.5f * widthPixels.toFloat
    val yCtrPixels: Float = 0.5f * heightPixels.toFloat

    val halfWidthPixels = xCtrPixels
    val halfHeightPixels = yCtrPixels
    val halfDimMin = math.min(halfWidthPixels, halfHeightPixels)

    val relOffset = worldPos - camera.worldPosition
    val x = xCtrPixels.toInt + math.round(relOffset.x.toFloat / camera.zoom_distUnitPerScreenUnit.toFloat * halfDimMin.toFloat)
    val y = yCtrPixels.toInt + math.round(relOffset.y.toFloat / camera.zoom_distUnitPerScreenUnit.toFloat * halfDimMin.toFloat)

    Vec2FixedPoint(x, heightPixels - y)
  }

}
