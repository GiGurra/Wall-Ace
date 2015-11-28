package se.gigurra.wallace.stage

trait StageInput[InputType] {
  def consumeInputs(inputs: Seq[InputType]): Seq[InputType]
  def update(): Unit
}
