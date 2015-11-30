package se.gigurra.wallace.input

import com.badlogic.gdx.Gdx

object InputArithmetics {

  implicit class RichInt(val key: Int) extends AnyVal {

    def isKeyDown: Boolean = Gdx.input.isKeyPressed(key)
    def isKeyUp: Boolean = !Gdx.input.isKeyPressed(key)

    def keyDown: Int = if (isKeyDown) 1 else 0
    def keyUp: Int = if (isKeyUp) 1 else 0
  }
}
