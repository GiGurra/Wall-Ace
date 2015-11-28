package se.gigurra.wallace.input

import com.badlogic.gdx.{Input, InputProcessor}
import se.gigurra.wallace.util.Vec2FixedPoint

import scala.collection.mutable.ArrayBuffer

class InputQue private() {

  def pop(): Seq[InputEvent] = synchronized {
    val out = events.toArray
    events.clear()
    out
  }

  private val events = new ArrayBuffer[InputEvent]()

  private def add(event: InputEvent): Boolean = synchronized {
    events += event
    true
  }

  private val processor = new InputProcessor {
    override def keyTyped(c: Char): Boolean = add(KeyType(c))
    override def mouseMoved(x: Int, y: Int): Boolean = add(MousePositionUpdate(false, Vec2FixedPoint(x, y)))
    override def keyDown(i: Int): Boolean = add(KeyUpdate(true, i))
    override def touchDown(x: Int, y: Int, pointer: Int, btn: Int): Boolean = add(MouseButtonUpdate(true, btn, Vec2FixedPoint(x, y)))
    override def keyUp(i: Int): Boolean = add(KeyUpdate(false, i))
    override def scrolled(n: Int): Boolean = add(MouseWheelScroll(n))
    override def touchUp(x: Int, y: Int, pointer: Int, btn: Int): Boolean = add(MouseButtonUpdate(false, btn, Vec2FixedPoint(x,y)))
    override def touchDragged(x: Int, y: Int, pointer: Int): Boolean = add(MousePositionUpdate(true, Vec2FixedPoint(x,y)))
  }
}

object InputQue {
  def capture(inputIfc: Input): InputQue = {
    val que = new InputQue
    inputIfc.setInputProcessor(que.processor)
    que
  }
}
