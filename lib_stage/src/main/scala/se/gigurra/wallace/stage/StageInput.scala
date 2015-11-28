package se.gigurra.wallace.stage

import java.io.Closeable

trait StageInput[InputType] extends Closeable {
  def consumeInputs(inputs: Seq[InputType]): Seq[InputType]
  def update(): Unit
  def close(): Unit
}
