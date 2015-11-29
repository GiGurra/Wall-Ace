package se.gigurra.wallace.stage

trait Stage[InputType] extends StageInput[InputType] {

  def stageId: String

  def consumeInput(input: InputType): Option[InputType] = {
    Some(input)
  }

  def update(): Unit = {}
  def onOpen(): Unit = {}

}
