package se.gigurra.wallace.render

import com.badlogic.gdx.graphics.GL20

case class RenderContext() {
  val renderState = new RenderState
  val renderAssets = new RenderAssets
  val renderShortcuts: GL20 = com.badlogic.gdx.Gdx.gl
}
