package se.gigurra.wallace.client.toplevelmanagers

import se.gigurra.wallace.input.InputEvent

trait TopLevelManager {
  def consumeInputs(inputs: Seq[InputEvent]): Seq[InputEvent]
  def update(): Unit
}
