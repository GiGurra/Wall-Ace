package se.gigurra.wallace.renderer

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import se.gigurra.wallace.renderer.Matrix4Stack.Matrix4Stack

class RenderState {
  val batch = new RichSpriteBatch(new SpriteBatch)
  val shapeRenderer = new ShapeRenderer
  val transform = new Matrix4Stack(32, { t =>
    batch.setTransformMatrix(t)
    shapeRenderer.setTransformMatrix(t)
  })
}
