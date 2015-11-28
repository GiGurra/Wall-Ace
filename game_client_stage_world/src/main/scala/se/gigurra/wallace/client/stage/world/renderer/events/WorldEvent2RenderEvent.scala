package se.gigurra.wallace.client.stage.world.renderer.events

import se.gigurra.wallace.client.stage.world.renderer.{NullRenderEvent, RenderEvent}
import se.gigurra.wallace.gamemodel._

object WorldEvent2RenderEvent {

  def apply(iSimFrame: WorldSimFrameIndex, we: WorldEvent): RenderEvent[WorldEvent] = {
    we match {
      case _ =>
        println(s"world2RenderEvent: Unknown WorldEvent type ${we.getClass}")
        NullRenderEvent(we)
    }
  }
}
