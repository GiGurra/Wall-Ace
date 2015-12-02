package se.gigurra.wallace.client.stage.world.renderer.elements

import se.gigurra.wallace.client.stage.world.renderer.events.{PlayerSpawnRenderEvent, WorldEvent2RenderEvent}
import se.gigurra.wallace.gamemodel.{WorldVector, WorldEvent, WorldSimFrameIndex}
import se.gigurra.wallace.renderer.{NullRenderEvent, RenderEvent, RenderContext, RenderAssets}
import se.gigurra.wallace.util.Time
import se.gigurra.wallace.client.stage.world.renderer._

case class EventsRenderer()(implicit renderContext: RenderContext[RenderAssets]) {

  private var events = Seq.empty[RenderEvent[WorldEvent]]

  import Renderables._
  import renderContext._

  def render(iSimFrame: WorldSimFrameIndex, worldEvents: Seq[WorldEvent]) = {
    implicit val t = Time.seconds
    events ++= worldEvents.flatMap(WorldEvent2RenderEvent(iSimFrame, _))
    events.foreach(draw)
    events = events.filterNot(_.expired)
  }

  def draw(event: RenderEvent[WorldEvent])(implicit t: Time.Seconds): Unit = {
    event match {
      case event: PlayerSpawnRenderEvent => draw(event)
      case event: NullRenderEvent[_] =>
      case event => println(s"Unhandled render event of type ${event.getClass}")
    }
  }

  def draw(event: PlayerSpawnRenderEvent)(implicit t: Time.Seconds): Unit = {
    drawSlidingEventText(
      s"Spawned ${event.name} at ${event.position}",
      event.position,
      event
    )
  }

  def drawSlidingEventText(str: String,
                           position: WorldVector,
                           event: RenderEvent[_])
                          (implicit t: Time.Seconds): Unit = {

    val text = assets.font20.prep(str, alphaScale = event.timeFractionLeft)

    transform(_
      .translate(position)
      .overrideScaleXY(0.05f / text.font.size)
      .translate(y = 150.0f * event.timeFractionInto)) {
      assets.temporary(text).foreach(_.draw())
    }
  }

}
