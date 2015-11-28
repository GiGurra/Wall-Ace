package se.gigurra.wallace.stage

trait Stage[InputType] {

  def stageId: String

  /**
    * @param inputs
    * @return Remaining InputEvents that were not consumed
    */
  def consumeInputs(inputs: Seq[InputType]): Seq[InputType] = {
    inputs
  }

  def update(): Unit = {

  }

  def onClose(): Unit = {

  }

  def onOpen(): Unit = {

  }


}
