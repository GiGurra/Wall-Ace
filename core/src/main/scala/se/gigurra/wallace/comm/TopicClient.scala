package se.gigurra.wallace.comm

case class Post[MessageType](topic: String, content: MessageType)

case class Subscribe(topic: String)

case class Unsubscribe(topic: String)

trait TopicClient[MessageType] {
  def subscribe(topic: String): Topic[MessageType]
  def unsubscribe(topic: String): Unit
  def post(topic: String, message: MessageType)
  def isConnected: Boolean
  def close(): Unit
}
