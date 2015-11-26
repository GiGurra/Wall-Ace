package se.gigurra.wallace.input

import se.gigurra.wallace.util.Vec2FixedPoint

sealed trait InputEvent
case class MousePositionUpdate(dragged: Boolean, position: Vec2FixedPoint) extends InputEvent
case class MouseButtonUpdate(pressed: Boolean, iButton: Int, position: Vec2FixedPoint) extends InputEvent
case class KeyUpdate(pressed: Boolean, iKey: Int) extends InputEvent
case class KeyType(character: Char) extends InputEvent
case object InputFocusLoss extends InputEvent
case object InputFocusGain extends InputEvent
case class MouseWheelScroll(amount: Int) extends InputEvent


