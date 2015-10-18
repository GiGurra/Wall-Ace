package se.gigurra.util

import com.badlogic.gdx.graphics.g2d.{Batch}
import com.badlogic.gdx.math.{Vector3, Matrix4}

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
      apply()
    }

    def pushPop(f: => Unit): Unit = {
      push()
      try {
        f
      } finally {
        pop()
      }
    }

    def transform(f: Matrix4 => Unit): this.type = { f(current); apply() }
    def translate(t: Vector3): this.type = transform(_.translate(t))
    def translate(x: Float = 0.0f, y: Float = 0.0f, z: Float = 0.0f): this.type = transform(_.translate(x, y, z))
    def scale(t: Vector3): this.type = transform(_.scale(t.x, t.y, t.z))
    def scale(x: Float = 0.0f, y: Float = 0.0f, z: Float = 0.0f): this.type = transform(_.scale(x, y, z))
    def rotate(angle: Float, x: Float = 0.0f, y: Float = 0.0f, z: Float = 0.0f): this.type = transform(_.rotate(angle, x, y, z))
    def rotate(angle: Float, axis: Vector3): this.type = transform(_.rotate(axis, angle))
    def apply(): this.type = { batch.setTransformMatrix(current); this }

  }

}