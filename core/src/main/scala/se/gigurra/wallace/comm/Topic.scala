package se.gigurra.wallace.comm

import rx.lang.scala.Observable

trait Topic[MessageType] {
  def name: String
  def stream: Observable[MessageType]
  def unsubscribe()
}
