package se.gigurra.wallace.client.stage.world.renderer.elements

import se.gigurra.wallace.gamemodel.Entity
import se.gigurra.wallace.renderer.{RenderContext, RenderAssets}

case class EntitiesRenderer()(implicit renderContext: RenderContext[RenderAssets]) {

  import Renderables._

  def render(seq: Seq[Entity]) = {

    import renderContext._

  }

}
