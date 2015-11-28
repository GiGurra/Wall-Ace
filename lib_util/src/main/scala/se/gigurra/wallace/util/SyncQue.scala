package se.gigurra.wallace.util

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

case class SyncQue[T:ClassTag]() {

  def pop(): Seq[T] = synchronized {
    val out = events.toArray
    events.clear()
    out
  }

  def add(t: T) = synchronized {
    events += t
  }

  private val events = new ArrayBuffer[T]()

}
