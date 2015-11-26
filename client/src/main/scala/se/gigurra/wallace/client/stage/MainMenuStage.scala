package se.gigurra.wallace.client.stage

import se.gigurra.wallace.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.input.InputEvent

class MainMenuStage(statCfg: StaticConfiguration,
                    dynCfg: DynamicConfiguration) extends Stage {

  override def id: String = "main-menu"

  override def consumeInputs(inputs: Seq[InputEvent]): Seq[InputEvent] = {
    inputs
  }

  override def update(): Unit = {
    // TODO: Draw stuff... Don't really act on anything .. or?? Network connections perhaps?
  }

}
