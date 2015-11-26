package se.gigurra.wallace.client.stage

import se.gigurra.wallace.input.InputEvent

trait Stage {

  def stageId: String

  /**
    * @param inputs
    * @return Remaining InputEvents that were not consumed
    */
  def consumeInputs(inputs: Seq[InputEvent]): Seq[InputEvent] = {
    inputs
  }

  def update(): Unit = {

  }

  def onClose(): Unit = {

  }

  def onOpen(): Unit = {

  }


}
