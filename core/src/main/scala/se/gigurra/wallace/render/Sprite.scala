package se.gigurra.wallace.render

import java.nio.ByteBuffer

import com.badlogic.gdx.graphics.Pixmap.Format
import com.badlogic.gdx.graphics.{Texture, Pixmap}
import se.gigurra.util.DecoratedTrait.DecoratedTrait

case class Sprite(imgData: Pixmap,
                  useMipMaps: Boolean,
                  disposeOnUpload: Boolean = false,
                  reloadOnContextLoss: Boolean = true) extends DecoratedTrait[Texture] {

  val textureData = TextureData.fromImgData(
    imgData = imgData,
    useMipMaps = useMipMaps,
    disposeOnUpload = disposeOnUpload,
    reloadOnContextLoss = reloadOnContextLoss)

  val texture = new Texture(textureData)

  def data: Pixmap = imgData

  def pixels: ByteBuffer = data.getPixels()

  def width: Int = imgData.getWidth

  def height: Int = imgData.getHeight

  def len: Int = pixels.capacity()

  def upload(): Unit = texture.load(textureData)

  override def _base0: Texture = texture
}

object Sprite {


  def fromFile(path: String,
               useMipMaps: Boolean,
               disposeOnUpload: Boolean = false,
               reloadOnContextLoss: Boolean = true) = {
    Sprite(
      imgData = ImageData.fromFile(path),
      useMipMaps = useMipMaps,
      disposeOnUpload = disposeOnUpload,
      reloadOnContextLoss = reloadOnContextLoss)
  }

  def fromSize(width: Int,
               height: Int,
               useMipMaps: Boolean,
               format: Format = Format.RGBA8888,
               disposeOnUpload: Boolean = false,
               reloadOnContextLoss: Boolean = true) = {
    Sprite(
      imgData = ImageData.fromSize(width, height, format),
      useMipMaps = useMipMaps,
      disposeOnUpload = disposeOnUpload,
      reloadOnContextLoss = reloadOnContextLoss)
  }

}