package se.gigurra.wallace.util

import java.util.concurrent.TimeUnit

import rx.lang.scala.Subject
import rx.lang.scala.schedulers.ExecutionContextScheduler

import scala.concurrent.duration.Duration

case class Topic[MessageType](
  name: String,
  historySize: Int = 128,
  historyTimeout: Duration = Duration(1, TimeUnit.HOURS)) {

  private val scheduler = ExecutionContextScheduler(scala.concurrent.ExecutionContext.Implicits.global)

  val (masterSink, masterSource) = makeSinkSource()

  def publish(message: MessageType): Unit = {
    masterSink.onNext(message)
  }

  def subscribe(): Subscription[MessageType] = {
    val (sink, source) = makeSinkSource()
    val cancelable = masterSource.subscribe(sink)
    Subscription[MessageType](name, source, () => { cancelable.unsubscribe(); sink.onCompleted() } )
  }

  def complete(): Unit = {
    masterSink.onCompleted()
  }

  private[this] def makeSinkSource() = {
    val sink = Subject[MessageType]()
    val source = sink.replay(historySize, historyTimeout, scheduler)
    source.connect
    (sink, source)
  }
}