package se.gigurra.wallace.stage

import se.gigurra.wallace.input.InputEvent

trait StageInput {
  def consumeInputs(inputs: Seq[InputEvent]): Seq[InputEvent]
  def update(): Unit
}
