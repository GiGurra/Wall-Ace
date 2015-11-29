package se.gigurra.wallace.client.stage.world.renderer

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import Matrix4Stack.Matrix4Stack
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class RenderState {
  val batch = new RichSpriteBatch(new SpriteBatch)
  val shapeRenderer = new ShapeRenderer
  val transform = new Matrix4Stack(32, { t =>
    batch.setTransformMatrix(t)
    shapeRenderer.setTransformMatrix(t)
  })
}
