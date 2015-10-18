package se.gigurra.wallace

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.PixmapTextureData
import com.badlogic.gdx.graphics.{GL20, Pixmap, Texture}
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.{Game, Gdx}

class Wallace extends Game {


  def loadImgFile(path: String) = {
    val fileHandle = Gdx.files.internal(path)
    new Pixmap(fileHandle)
  }

  def mkTextureData(imgData: Pixmap,
                    useMipMaps: Boolean,
                    disposeOnUpload: Boolean = false,
                    reloadOnContextLoss: Boolean = true) = {
    new PixmapTextureData(
      imgData, // pixmap
      null, // format (null -> read from img data instead)
      useMipMaps, // useMipMaps
      disposeOnUpload, // disposePixmap
      reloadOnContextLoss // managed
    )
  }

  case class Sprite(imgData: Pixmap,
                    useMipMaps: Boolean,
                    disposeOnUpload: Boolean = false,
                    reloadOnContextLoss: Boolean = true) {

    val textureData = mkTextureData(
      imgData = imgData,
      useMipMaps = useMipMaps,
      disposeOnUpload = disposeOnUpload,
      reloadOnContextLoss = reloadOnContextLoss)

    val texture = new Texture(textureData)

    def data = imgData.getPixels
    def width = imgData.getWidth
    def height = imgData.getHeight
    def len = data.capacity()
  }

  object Sprite {
    def fromFile(path: String,
                 useMipMaps: Boolean,
                 disposeOnUpload: Boolean = false,
                 reloadOnContextLoss: Boolean = true) = {
      Sprite(
        imgData = loadImgFile(path),
        useMipMaps = useMipMaps,
        disposeOnUpload = disposeOnUpload,
        reloadOnContextLoss = reloadOnContextLoss)
    }
  }

  lazy val created = new {
    val batch = new SpriteBatch
    val sprite = Sprite.fromFile("libgdxlogo.png", useMipMaps = false)
  }

  override def create(): Unit = {}

  override def render(): Unit = {
    import created._
    Gdx.gl.glClearColor(0.4f + MathUtils.random() * 0.2f, 0.4f + MathUtils.random() * 0.2f, 0.4f + MathUtils.random() * 0.2f, 1f)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    batch.begin()
    batch.draw(sprite.texture, (Gdx.graphics.getWidth - sprite.width) / 2f, (Gdx.graphics.getHeight - sprite.height) / 2f)
    batch.end()

  }

  override def resize(width: Int, height: Int) {
    super.resize(width, height)
    if (screen != null) screen.resize(width, height)
  }
}
