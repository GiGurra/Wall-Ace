package se.gigurra.wallace.render

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.graphics.g2d.{BitmapFont, GlyphLayout, PixmapPacker}
import com.badlogic.gdx.math.Vector2
import se.gigurra.util.Decorated.Decorated

object Font {

  class Font(_base: BitmapFont) extends Decorated[BitmapFont](_base) {

    def layout(str: CharSequence): GlyphLayout = {
      new GlyphLayout(this, str)
    }

    def size(str: CharSequence): Vector2 = {
      val l = layout(str)
      new Vector2(l.width, l.height)
    }

    def width(str: CharSequence): Float = {
      size(str).x
    }

    def height(str: CharSequence): Float = {
      size(str).y
    }

  }

  /**
   * See https://github.com/libgdx/libgdx/wiki/Gdx-freetype
   * For parameter descriptions
   */
  def fromTtfFile(filePath: String,
                  size: Int = 20,
                  color: Color = Color.WHITE,
                  borderWidth: Float = 0.0f,
                  borderColor: Color = Color.BLACK,
                  borderStraight: Boolean = false,
                  shadowOffsetX: Int = 0,
                  shadowOffsetY: Int = 0,
                  shadowColor: Color = new Color(0, 0, 0, 0.75f),
                  characters: String = FreeTypeFontGenerator.DEFAULT_CHARS,
                  kerning: Boolean = true,
                  packer: PixmapPacker = null,
                  flip: Boolean = false,
                  genMipMaps: Boolean = false,
                  minFilter: TextureFilter = TextureFilter.Nearest,
                  magFilter: TextureFilter = TextureFilter.Nearest): Font = {

    val generator = new FreeTypeFontGenerator(Gdx.files.internal(filePath))
    val parameter = new FreeTypeFontParameter()

    parameter.size = size

    parameter.color = color
    parameter.borderWidth = borderWidth
    parameter.borderColor = borderColor
    parameter.borderStraight = borderStraight
    parameter.shadowOffsetX = shadowOffsetX
    parameter.shadowOffsetY = shadowOffsetY
    parameter.shadowColor = shadowColor
    parameter.characters = characters
    parameter.kerning = kerning
    parameter.packer = packer
    parameter.flip = flip
    parameter.genMipMaps = genMipMaps
    parameter.minFilter = minFilter
    parameter.magFilter = magFilter

    val font = generator.generateFont(parameter)
    generator.dispose()
    new Font(font)
  }
}
