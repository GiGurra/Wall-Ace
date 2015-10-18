package se.gigurra.wallace.render

import com.badlogic.gdx.graphics.{Texture, Pixmap}

case class Sprite(imgData: Pixmap,
                  useMipMaps: Boolean,
                  disposeOnUpload: Boolean = false,
                  reloadOnContextLoss: Boolean = true) {
  val textureData = TextureData.fromImgData(
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
      imgData = ImageData.fromFile(path),
      useMipMaps = useMipMaps,
      disposeOnUpload = disposeOnUpload,
      reloadOnContextLoss = reloadOnContextLoss)
  }
}