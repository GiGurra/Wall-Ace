package se.gigurra.wallace

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera}
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.{Game, Gdx}
import se.gigurra.util.Matrix4Stack.Matrix4Stack
import se.gigurra.wallace.render.{Font, Sprite}

case class WallaceState() {
  implicit val batch = new SpriteBatch
  val cam = new OrthographicCamera
  val sprite = Sprite.fromFile("libgdxlogo.png", useMipMaps = false)
  val transform = new Matrix4Stack(32, batch)
  val font20 = Font.fromTtfFile("fonts/pt-mono/PTM55FT.ttf", size = 20)
}

class Wallace extends Game {

  lazy val state = WallaceState()

  override def create(): Unit = {}

  override def render(): Unit = {
    import state._

    Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 0)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    batch.begin()

    batch.draw(sprite.texture, -sprite.width / 2.0f, -sprite.height / 2f)

    val text = s"Fps: $fps"
    val textLayout = font20.layout(text)
    transform.pushPop {
      transform.translate(-font20.width(text) / 2.0f, 0.0f, 0.0f)
      font20.draw(batch, textLayout, 0f, 0f)
    }
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

  def fps = Gdx.graphics.getFramesPerSecond

}
