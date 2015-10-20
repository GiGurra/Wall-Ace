package se.gigurra.wallace.comm

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.Duration

case class Post[MessageType](topic: String, content: MessageType)

case class Subscribe(topic: String)

case class Unsubscribe(topic: String)

trait SubscriptionClient[MessageType] {

  def subscribe(
    name: String,
    historySize: Int = 128,
    historyTimeout: Duration = Duration.apply(1, TimeUnit.HOURS)): Subscription[MessageType]

  def unsubscribe(topic: String): Unit

  def post(topic: String, message: MessageType)

  def isConnected: Boolean

  def close(): Unit
}
