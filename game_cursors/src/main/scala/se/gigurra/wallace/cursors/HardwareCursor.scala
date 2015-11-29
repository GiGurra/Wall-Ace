package se.gigurra.wallace.cursors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.{Pixmap, Cursor}
import se.gigurra.wallace.client.stage.world.renderer.{ImageData, Sprite}

class SettableHardwareCursor(name: String, cursor: Cursor, visible: Boolean = true) {
  def set(): Unit = {
    if (HardwareCursor.current != this) {
      Gdx.graphics.setCursor(cursor)
      HardwareCursor.isVisible = visible
      HardwareCursor.current = this
    }
  }
}

trait HardwareCursor {
  protected final def _systemDefault: Cursor = null
  protected def _default: Cursor
  protected def _aim: Cursor
  protected def _button: Cursor
  protected def _invisible: Cursor

  final val systemDefault = new SettableHardwareCursor("sysdefault", _systemDefault)
  final val default = new SettableHardwareCursor("default", _default)
  final val aim = new SettableHardwareCursor("aim", _aim)
  final val button = new SettableHardwareCursor("button", _button)
  final val invisible = new SettableHardwareCursor("invisible", _invisible, visible = false)
}

object HardwareCursor {

  @volatile var isVisible = false
  @volatile var current: SettableHardwareCursor = null

  lazy val all: HardwareCursor =
    new HardwareCursor {
      override protected def _default: Cursor = _systemDefault
      override protected def _button: Cursor = _systemDefault
      override protected def _aim: Cursor = makeCursor("crosshair.png", 0.5f, 0.5f)
      override protected def _invisible: Cursor = {
        val emptySprite = Sprite.fromSize(64, 64, false)
        Gdx.graphics.newCursor(emptySprite.pixmap, 0, 0)
      }
    }

  def makeCursor(imgPath: String, xs: Float, ys: Float): Cursor = {
    makeCursor(ImageData.fromFile(imgPath), xs, ys)
  }

  def findFirstSquareLargerThan(min: Int): Int = {
    var exp = 1

    def x = math.pow(2, exp).toInt

    while (x < min) {
      exp += 1
    }
    x
  }

  def makeCursor(unresizedMap: Pixmap, xs: Float, ys: Float): Cursor = {

    val xNew = findFirstSquareLargerThan(unresizedMap.getWidth)
    val yNew = findFirstSquareLargerThan(unresizedMap.getHeight)

    val pixmap = ImageData.fromSize(xNew, yNew)
    pixmap.drawPixmap(unresizedMap, 0, 0)

    Gdx.graphics.newCursor(
      pixmap,
      math.round(xs * unresizedMap.getWidth.toFloat),
      math.round(ys * unresizedMap.getHeight.toFloat))
  }

  def systemDefault = all.systemDefault
  def default = all.default
  def aim = all.aim
  def button = all.button
  def invisible = all.invisible
}
