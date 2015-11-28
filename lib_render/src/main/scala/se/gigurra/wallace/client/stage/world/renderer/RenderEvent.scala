package se.gigurra.wallace.client.stage.world.renderer

import se.gigurra.wallace.util.Time
import se.gigurra.wallace.util.Time.Seconds

trait RenderEvent[SourceType] {

  def source: SourceType

  def startTime: Time.Seconds
  def lifeTime: Time.Seconds
  def endTime: Time.Seconds = startTime + lifeTime

  def expired(time: Time.Seconds = Time.seconds): Boolean = {
    time > endTime
  }
}

case class NullRenderEvent[SourceType](_source: SourceType) extends RenderEvent[SourceType] {

  override def source: SourceType = _source

  override def lifeTime: Seconds = 0

  override def startTime: Seconds = 0

}
