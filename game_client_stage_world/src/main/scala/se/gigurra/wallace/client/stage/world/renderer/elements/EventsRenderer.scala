package se.gigurra.wallace.client.stage.world.renderer.elements

import se.gigurra.wallace.client.stage.world.renderer.events.{PlayerSpawnRenderEvent, WorldEvent2RenderEvent}
import se.gigurra.wallace.gamemodel.{WorldEvent, WorldSimFrameIndex}
import se.gigurra.wallace.renderer.{NullRenderEvent, RenderEvent, RenderContext, RenderAssets}
import se.gigurra.wallace.util.Time
import se.gigurra.wallace.client.stage.world.renderer._

case class EventsRenderer()(implicit renderContext: RenderContext[RenderAssets]) {

  private var events = Seq.empty[RenderEvent[WorldEvent]]

  import Renderables._
  import renderContext._

  def render(iSimFrame: WorldSimFrameIndex, worldEvents: Seq[WorldEvent]) = {
    val t = Time.seconds
    events ++= worldEvents.flatMap(WorldEvent2RenderEvent(iSimFrame, _))
    events.foreach(draw(_, t))
    events = events.filterNot(_.expired(t))
  }

  def draw(event: RenderEvent[WorldEvent], t: Time.Seconds): Unit = {
    event match {
      case event: PlayerSpawnRenderEvent => draw(event, t)
      case event: NullRenderEvent[_] =>
      case event => println(s"Unhandled render event of type ${event.getClass}")
    }
  }

  def draw(event: PlayerSpawnRenderEvent, t: Time.Seconds): Unit = {

    val text = assets.font20.prep(
      str = s"Spawned ${event.source.name} at ${event.source.position}",
      alphaScale = event.timeFractionLeft(t))

    transform(_
      .translate(event.source.position)
      .overrideScaleXY(0.05f / text.font.size)
      .translate(y = 100.0f * event.timeFractionInto(t))) {
      assets.temporary(text).foreach(_.draw())
    }
  }

}
