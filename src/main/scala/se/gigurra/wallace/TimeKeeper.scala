package se.gigurra.wallace

import se.gigurra.wallace.gamestate.ServerStepMessage

case class Time(
    iterationIndex: Long, 
    localTime: Double, 
    estimatedServerTime: Double)

class TimeKeeper {
  import TimeKeeper._

  private var estimatedServerTime = 0.0
  private var lastLocalTime = 0.0
  private var iterationIndex = 0L
  private var lowestDelta = INIT_TIME_DELTA

  def update(stepMessageFromServer: Option[ServerStepMessage]): Time = {

    lastLocalTime = calcLocalTime()

    stepMessageFromServer foreach { stepMessageFromServer =>
      val newDelta = lastLocalTime - stepMessageFromServer.getServerTime
      if (lowestDelta == INIT_TIME_DELTA || newDelta < lowestDelta) {
        lowestDelta = newDelta
      }
    }

    estimatedServerTime = lastLocalTime + lowestDelta
    iterationIndex += 1

    Time(iterationIndex, lastLocalTime, estimatedServerTime)

  }

  private def calcLocalTime(): Double = {
    System.nanoTime() / 1e9
  }
}

object TimeKeeper {
  val INIT_TIME_DELTA = 0.0
}