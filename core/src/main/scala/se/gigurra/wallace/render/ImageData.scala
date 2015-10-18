package se.gigurra.wallace.render

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Pixmap.Format

object ImageData {

  def fromFile(path: String) = {
    val fileHandle = Gdx.files.internal(path)
    new Pixmap(fileHandle)
  }

  def fromSize(width: Int, height: Int, format: Format = Format.RGBA8888) = {
    require(width > 0)
    require(height > 0)
    new Pixmap(width, height, format)
  }

}
