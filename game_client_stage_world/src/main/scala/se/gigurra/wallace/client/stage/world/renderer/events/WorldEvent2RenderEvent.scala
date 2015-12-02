package se.gigurra.wallace.client.stage.world.renderer.events

import se.gigurra.wallace.gamemodel._
import se.gigurra.wallace.renderer.{NullRenderEvent, RenderEvent}
import se.gigurra.wallace.util.Time.Seconds
import se.gigurra.wallace.util.Time

case class PlayerSpawnRenderEvent(source: PlayerSpawn) extends RenderEvent[PlayerSpawn] {

  override def lifeTime: Seconds = 2.5

  override val startTime: Seconds = Time.seconds
}

object WorldEvent2RenderEvent {

  def apply(iSimFrame: WorldSimFrameIndex, we: WorldEvent): Seq[RenderEvent[WorldEvent]] = {
    we match {
      case p : PlayerSpawn =>
        Seq(PlayerSpawnRenderEvent(p))
      case _ =>
        println(s"world2RenderEvent: Unknown WorldEvent type ${we.getClass}")
        Seq(NullRenderEvent(we))
    }
  }
}
