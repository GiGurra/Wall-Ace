package se.gigurra.wallace.client.stage.world.renderer

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch

object Projections {

  def ortho11(w: Int, h: Int)(implicit renderContext: RenderContext[_]) : Unit = {

    val cam = new OrthographicCamera

    if (w > h) {
      cam.viewportHeight = 2.0f
      cam.viewportWidth = 2.0f * w.toFloat/h.toFloat
    } else {
      cam.viewportWidth = 2.0f
      cam.viewportHeight = 2.0f * h.toFloat/w.toFloat
    }

    cam.update()
    renderContext.state.batch.setProjectionMatrix(cam.combined)
    renderContext.shapeRenderer.setProjectionMatrix(cam.combined)
  }
}
