package se.gigurra.wallace

import com.badlogic.gdx.graphics.Color

package object renderer {

  implicit class RichGdxColor(val color: Color) extends AnyVal {
    def scaleAlpha(s: Float): Color = new Color(color.r, color.g, color.b, color.a * s)
  }
}
