package se.gigurra.wallace.client.toplevelmanagers

import se.gigurra.wallace.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.input.InputEvent

case class ClientWindowManger(statCfg: StaticConfiguration, dynCfg: DynamicConfiguration) extends TopLevelManager {

  /**
    * @param inputs
    * @return Remaining InputEvents that were not consumed
    */
  def consumeInputs(inputs: Seq[InputEvent]): Seq[InputEvent] = {
    inputs
  }

  def update(): Unit = {
    // TODO: Something. Resize window if requested?
  }

}
