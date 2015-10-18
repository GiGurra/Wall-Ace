package se.gigurra.wallace.render

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import se.gigurra.util.Decorated.Decorated

class RichSpriteBatch(_base: SpriteBatch) extends Decorated[SpriteBatch](_base) {
  def active(f: => Unit): Unit = {
    this.begin()
    try {
      f
    } finally {
      this.end()
    }
  }

  def apply(f: => Unit): Unit = {
    active(f)
  }
}
