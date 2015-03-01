package se.gigurra.wallace.client

import se.gigurra.renderer.Renderer
import se.gigurra.renderer.Renderer.toTransform
import se.gigurra.renderer.Renderer.toTransformStack
import se.gigurra.wallace.client.widgets.TestWidget
import se.gigurra.wallace.gamestate.World

class IngameGraphics {

  val testWidget = new TestWidget

  def draw(
    renderer: Renderer,
    world: World) {

    renderer.setViewPortToFull()
    renderer.setLineWidth(2.0f)

    renderer.loadIdentity().orthoMinDist(1, renderer.surfaceSize)

    renderer.pushPopTransform {
      renderer.translate2d(0.75f, 0.75f)
      renderer.scale2d(0.25f)
      renderer.draw(testWidget)
    }

  }

}

