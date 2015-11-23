package se.gigurra.wallace.client.renderer

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{Batch, GlyphLayout}
import com.badlogic.gdx.math.{Matrix4, Vector3}

import scala.language.implicitConversions

object Matrix4Stack {

  class Matrix4Stack(depth: Int, batch: Batch) {
    private val stack = (0 until depth).map(_ => new Matrix4).toArray
    private var i = 0

    def current = stack(i)

    def next = stack(i + 1)

    def push() = {
      require(i + 1 < depth)
      next.set(current)
      i += 1
    }

    def pop() = {
      require(i > 0)
      i -= 1
      upload()
    }

    def pushPop(f: => Unit): Unit = {
      push()
      try {
        f
      } finally {
        pop()
      }
    }

    def apply[AnyReturn](ftr: Matrix4Stack => AnyReturn)(f: => Unit): Unit = {
      pushPop({ftr(this); f})
    }

    def unitSize(texture: RenderAsset[_]) = scale(
      1.0f / math.min(texture.width, texture.height),
      1.0f / math.min(texture.width, texture.height))

    def unitSize(texture: GlyphLayout) = scale(
      1.0f / math.min(texture.width, texture.height),
      1.0f / math.min(texture.width, texture.height))

    def center(texture: RenderAsset[_]) = translate(-texture.width/2.0f, -texture.height/2.0f)
    def center(text: GlyphLayout) = translate(-text.width/2.0f, text.height/2.0f)
    def centerX(texture: Texture) = translate(-texture.getWidth.toFloat/2.0f, 0.0f)
    def centerX(text: GlyphLayout) = translate(-text.width/2.0f, 0.0f)
    def centerY(texture: Texture) = translate(0.0f, -texture.getHeight.toFloat/2.0f)
    def centerY(text: GlyphLayout) = translate(0.0f, text.height/2.0f)

    def upload(): this.type = { batch.setTransformMatrix(current); this }
    def transform(f: Matrix4 => Unit) = { f(current); upload() }
    def translate(t: Vector3) = transform(_.translate(t))
    def translate(x: Float = 0.0f, y: Float = 0.0f, z: Float = 0.0f) = transform(_.translate(x, y, z))
    def scale(t: Vector3) = transform(_.scale(t.x, t.y, t.z))
    def scale(x: Float = 1.0f, y: Float = 1.0f, z: Float = 1.0f) = transform(_.scale(x, y, z))
    def scalexy(s: Float) = scale(x = s, y = s)
    def rotate(angle: Float, x: Float = 0.0f, y: Float = 0.0f, z: Float = 0.0f) = transform(_.rotate(angle, x, y, z))
    def rotate(angle: Float, axis: Vector3) = transform(_.rotate(axis, angle))

  }

}