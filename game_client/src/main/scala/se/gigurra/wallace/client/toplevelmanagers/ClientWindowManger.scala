package se.gigurra.wallace.client.toplevelmanagers

import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.input.InputEvent
import se.gigurra.wallace.stage.StageInput

case class ClientWindowManger(statCfg: StaticConfiguration, dynCfg: DynamicConfiguration)
  extends StageInput[InputEvent] {

  override def consumeInput(input: InputEvent): Option[InputEvent] = {
    Some(input)
  }

  override def update(): Unit = {
    // TODO: Something. Resize window if requested?
  }

  override def close(): Unit = {

  }

}
