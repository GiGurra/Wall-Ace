package se.gigurra.wallace.cursors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Cursor
import se.gigurra.wallace.client.stage.world.renderer.{ImageData, Sprite}

class SettableHardwareCursor(cursor: Cursor, visible: Boolean = true) {
  def set(): Unit = {
    Gdx.graphics.setCursor(cursor)
    HardwareCursor.isVisible = visible
  }
}

trait HardwareCursor {
  protected final def _systemDefault: Cursor = null
  protected def _default: Cursor
  protected def _aim: Cursor
  protected def _button: Cursor
  protected def _invisible: Cursor

  final val systemDefault = new SettableHardwareCursor(_systemDefault)
  final val default = new SettableHardwareCursor(_default)
  final val aim = new SettableHardwareCursor(_aim)
  final val button = new SettableHardwareCursor(_button)
  final val invisible = new SettableHardwareCursor(_invisible, visible = false)
}

object HardwareCursor {

  @volatile var isVisible = false

  val crosshairImage = ImageData.fromFile("crosshair.png")

  lazy val all: HardwareCursor =
    new HardwareCursor {
      override protected def _default: Cursor = _systemDefault
      override protected def _button: Cursor = _systemDefault
      override protected def _aim: Cursor = _systemDefault
      override protected def _invisible: Cursor = {
        val emptySprite = Sprite.fromSize(64, 64, false)
        Gdx.graphics.newCursor(emptySprite.pixmap, 0, 0)
      }
    }

  def systemDefault = all.systemDefault
  def default = all.default
  def aim = all.aim
  def button = all.button
  def invisible = all.invisible
}
