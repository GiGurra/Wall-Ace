package se.gigurra.wallace.comm

case class Post[MessageType](topic: String, content: MessageType)
case class Subscribe(topic: String)
case class Subscribed(topic: String)
case class Unsubscribe(topic: String)
case class Unsubscribed(topic: String)
