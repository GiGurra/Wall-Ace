package se.gigurra.wallace

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.{OrthographicCamera, GL20}
import com.badlogic.gdx.math.{Matrix4, MathUtils}
import com.badlogic.gdx.{Game, Gdx}
import se.gigurra.wallace.render.Sprite

case class WallaceState() {
  val batch = new SpriteBatch
  val cam = new OrthographicCamera
  val sprite = Sprite.fromFile("libgdxlogo.png", useMipMaps = false)
}

class Wallace extends Game {

  lazy val state = WallaceState()

  override def create(): Unit = {}

  override def render(): Unit = {
    import state._
    Gdx.gl.glClearColor(0.4f + MathUtils.random() * 0.2f, 0.4f + MathUtils.random() * 0.2f, 0.4f + MathUtils.random() * 0.2f, 1f)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    batch.begin()
    batch.draw(sprite.texture, -sprite.width / 2.0f, -sprite.height / 2f)
    batch.end()
  }

  override def resize(w: Int, h: Int) {
    import state._
    cam.viewportWidth = w.toFloat
    cam.viewportHeight = h.toFloat
    cam.update()
    batch.setProjectionMatrix(cam.combined)
    batch.setTransformMatrix(new Matrix4())
  }
}
