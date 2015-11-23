package se.gigurra.wallace.client.renderer

import com.badlogic.gdx.graphics.g2d.GlyphLayout

case class RichGlyphLayout(layout: GlyphLayout, font: Font) {
  def height = layout.height
  def width = layout.width
}