package se.gigurra.util

import scala.language.implicitConversions

object Decorated {

  class Decorated[BaseType](val _base: BaseType) {
    implicit def toBase: BaseType = Decorated.toBase(this)
  }

  implicit def toBase[BaseType](decorated: Decorated[BaseType]): BaseType = decorated._base
}

object DecoratedTrait {

  type DecoratedTrait[T] = DecoratedTrait0[T]

  trait DecoratedTrait0[BaseType] {
    def _base0: BaseType
    implicit def toBase0: BaseType = DecoratedTrait.toBase0(this)
  }

  implicit def toBase0[BaseType](decorated: DecoratedTrait0[BaseType]): BaseType = decorated._base0


  trait DecoratedTrait1[BaseType] {
    def _base1: BaseType
    implicit def toBase1: BaseType = DecoratedTrait.toBase1(this)
  }

  implicit def toBase1[BaseType](decorated: DecoratedTrait1[BaseType]): BaseType = decorated._base1
}