package se.gigurra.wallace.util

import rx.lang.scala.Observable

case class Subscription[MessageType](
  name: String,
  stream: Observable[MessageType],
  unsubscribe: () => Unit) {

  def awaitComplete() = {
    stream.toBlocking.foreach(_ => {})
  }

}
