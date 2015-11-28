package se.gigurra.wallace.util

case class MeasuredOperation[ReturnType](returnValue: ReturnType,
                                         dt: Time.Seconds) extends Decorated[ReturnType](returnValue)

object Time {

  type Seconds = Double

  type Milliseconds = Long

  def seconds: Seconds = System.nanoTime / 1e9

  def millis: Milliseconds = System.currentTimeMillis

  def measure[ReturnType](operation: => ReturnType): MeasuredOperation[ReturnType] = {
    val t0 = seconds
    val returnValue = operation
    val t1 = seconds
    MeasuredOperation(returnValue, t1-t0)
  }

}
