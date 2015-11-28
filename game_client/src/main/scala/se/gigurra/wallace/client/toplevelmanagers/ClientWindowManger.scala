package se.gigurra.wallace.client.toplevelmanagers

import se.gigurra.wallace.config.client.DynamicConfiguration
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.input.InputEvent
import se.gigurra.wallace.stage.StageInput

case class ClientWindowManger(statCfg: StaticConfiguration, dynCfg: DynamicConfiguration)
  extends StageInput[InputEvent] {

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
