package se.gigurra.wallace.renderer

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.GlyphLayout

case class RichGlyphLayout(layout: GlyphLayout,
                           font: Font,
                           colorOverride: Option[Color] = None) {

  def height = layout.height
  def width = layout.width
}