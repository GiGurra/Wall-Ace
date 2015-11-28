package se.gigurra.wallace.client.stage.world.renderer.elements

import se.gigurra.wallace.client.stage.world.renderer._
import se.gigurra.wallace.client.stage.world.renderer.events.WorldEvent2RenderEvent
import se.gigurra.wallace.gamemodel.{WorldEvent, WorldSimFrameIndex}
import se.gigurra.wallace.util.Time

case class EventsRenderer()(implicit renderContext: RenderContext[RenderAssets]) {

  private var events = Seq.empty[RenderEvent[WorldEvent]]

  import Renderables._

  def render(iSimFrame: WorldSimFrameIndex, worldEvents: Seq[WorldEvent]) = {

    import renderContext._

    events ++= worldEvents.map(WorldEvent2RenderEvent(iSimFrame, _))

    // TODO: Some drawing here..

    val t = Time.seconds
    events = events.filterNot(_.expired(t))
  }

}
