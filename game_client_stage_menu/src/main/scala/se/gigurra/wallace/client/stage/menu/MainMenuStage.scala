package se.gigurra.wallace.client.stage.menu

import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.input.InputEvent
import se.gigurra.wallace.stage.{Stage, StageManager}

class MainMenuStage(statCfg: StaticConfiguration,
                    dynCfg: DynamicConfiguration,
                    stageManager: StageManager) extends Stage {

  override def stageId: String = "main-menu"

  override def consumeInputs(inputs: Seq[InputEvent]): Seq[InputEvent] = {
    inputs
  }

  override def update(): Unit = {
    // TODO: Draw stuff... Don't really act on anything .. or?? Network connections perhaps?
  }

}
