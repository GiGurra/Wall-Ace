package se.gigurra.wallace.render

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import se.gigurra.util.Matrix4Stack.Matrix4Stack

class RenderState {
  val batch = new RichSpriteBatch(new SpriteBatch)
  val transform = new Matrix4Stack(32, batch)
}
