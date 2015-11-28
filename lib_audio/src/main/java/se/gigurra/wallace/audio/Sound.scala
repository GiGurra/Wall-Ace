package se.gigurra.wallace.audio

import se.gigurra.wallace.util.Time

trait Sound {
  def startTime: Time.Seconds
  def timeLeft(time: Time.Seconds = Time.seconds): Time.Seconds
  def sourceLength: Time.Seconds
  def volume: Float
  def addPlayTime(dt: Time.Seconds)
  def stopNow(fadeTime: Time.Seconds = 0.0)
  def stopAfter(dt: Time.Seconds, fadeTime: Time.Seconds = 0.0)
  def expired(time: Time.Seconds = Time.seconds): Boolean = timeLeft(time) > 0
}
