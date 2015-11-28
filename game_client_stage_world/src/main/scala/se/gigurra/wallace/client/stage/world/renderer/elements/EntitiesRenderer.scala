package se.gigurra.wallace.client.stage.world.renderer.elements

import se.gigurra.wallace.client.stage.world.renderer.{RenderAssets, RenderContext, Rendering}
import se.gigurra.wallace.gamemodel.{Entity, Terrain}

case class EntitiesRenderer()(implicit renderContext: RenderContext[RenderAssets]) {

  import Renderables._

  def render(seq: Seq[Entity]) = {

    import renderContext._

  }

}
