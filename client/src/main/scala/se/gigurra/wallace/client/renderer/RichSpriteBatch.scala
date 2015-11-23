package se.gigurra.wallace.client.renderer

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import se.gigurra.wallace.util.Decorated

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
