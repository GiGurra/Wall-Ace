package se.gigurra.wallace.render

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch

object Projections {

  def ortho11(batch: Batch, w: Int, h: Int): Unit = {

    val cam = new OrthographicCamera

    if (w > h) {
      cam.viewportHeight = 1.0f
      cam.viewportWidth = 1.0f * w.toFloat/h.toFloat
    } else {
      cam.viewportWidth = 1.0f
      cam.viewportHeight = 1.0f * h.toFloat/w.toFloat
    }

    cam.update()
    batch.setProjectionMatrix(cam.combined)
  }
}
