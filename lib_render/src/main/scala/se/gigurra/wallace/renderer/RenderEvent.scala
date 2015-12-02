package se.gigurra.wallace.renderer

import se.gigurra.wallace.util.Time
import se.gigurra.wallace.util.Time.Seconds

trait RenderEvent[+SourceType] {

  def source: SourceType

  val startTime: Time.Seconds
  def lifeTime: Time.Seconds
  def endTime: Time.Seconds = startTime + lifeTime

  def timeFractionInto(time: Time.Seconds = Time.seconds): Float = {
    ((time - startTime) / lifeTime).toFloat
  }

  def timeFractionLeft(time: Time.Seconds = Time.seconds): Float = {
    1.0f - timeFractionInto(time)
  }

  def expired(time: Time.Seconds = Time.seconds): Boolean = {
    time > endTime
  }
}

case class NullRenderEvent[SourceType](_source: SourceType) extends RenderEvent[SourceType] {

  override def source: SourceType = _source

  override def lifeTime: Seconds = 0

  override val startTime: Seconds = 0

}
