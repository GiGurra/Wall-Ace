package se.gigurra.wallace.network.kryoimpl

import scala.reflect.ClassTag

trait SerialRegisterable {
  def register[T: ClassTag](): Unit
  def register[T](cls: Class[T]): Unit = {
    register()
  }
}
