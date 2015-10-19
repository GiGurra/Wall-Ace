package se.gigurra.renderer

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import Matrix4Stack.Matrix4Stack

class RenderState {
  val batch = new RichSpriteBatch(new SpriteBatch)
  val transform = new Matrix4Stack(32, batch)
}
