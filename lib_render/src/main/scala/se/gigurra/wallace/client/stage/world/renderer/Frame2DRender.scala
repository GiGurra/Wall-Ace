package se.gigurra.wallace.client.stage.world.renderer

import com.badlogic.gdx.Gdx

trait Frame2DRender {

  def frame2D[AssetsType](impl: => Unit)(implicit context: RenderContext[AssetsType]): Unit = {
    import context.state._
    import context.glShortcuts._
    import com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT
    batch {
      Projections.ortho11(Gdx.graphics.getWidth, Gdx.graphics.getHeight)
      glClearColor(0.0f, 0.0f, 0.0f, 0)
      glClear(GL_COLOR_BUFFER_BIT)
      impl
    }
  }
}
