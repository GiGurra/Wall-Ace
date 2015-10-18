package se.gigurra.util

import scala.language.implicitConversions

object Decorated {

  abstract class Decorated[BaseType](val _base: BaseType) {
    implicit def toBase: BaseType = Decorated.toBase(this)
  }

  implicit def toBase[BaseType](decorated: Decorated[BaseType]): BaseType = decorated._base
}