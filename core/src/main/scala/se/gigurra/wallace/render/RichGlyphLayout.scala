package se.gigurra.wallace.render

import com.badlogic.gdx.graphics.g2d.GlyphLayout
import se.gigurra.util.Decorated.Decorated

class RichGlyphLayout(_base: GlyphLayout, val font: Font) extends Decorated[GlyphLayout](_base)