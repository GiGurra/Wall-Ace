package se.gigurra.wallace.stage

import java.io.Closeable

import scala.annotation.tailrec

trait StageInput[InputType] extends Closeable {
  def consumeInput(inputs: InputType): Option[InputType]
  def update(): Unit
  def close(): Unit = {}
}

object StageInput {

  @tailrec
  def consume[InputType](consumers: Seq[StageInput[InputType]],
                         input: InputType): Option[InputType] = {

    if (consumers.isEmpty) {
      Some(input)
    } else {
      consumers.head.consumeInput(input) match {
        case None => None
        case Some(input) => consume(consumers.tail, input)
      }
    }
  }

  implicit class RichInputConsumers[InputType](val consumers: Seq[StageInput[InputType]])
    extends AnyVal {

    def consume(input: InputType): Option[InputType] = {
      StageInput.consume(consumers, input)
    }
  }

}
