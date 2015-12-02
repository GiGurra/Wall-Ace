package se.gigurra.wallace.renderer

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.glutils.PixmapTextureData

object TextureData {

  def fromImgData(imgData: Pixmap,
                  useMipMaps: Boolean,
                  disposeOnUpload: Boolean = false,
                  reloadOnContextLoss: Boolean = true): PixmapTextureData = {
    new PixmapTextureData(
      imgData, // pixmap
      null, // format (null -> read from img data instead)
      useMipMaps, // useMipMaps
      disposeOnUpload, // disposePixmap
      reloadOnContextLoss // managed
    )
  }

}
