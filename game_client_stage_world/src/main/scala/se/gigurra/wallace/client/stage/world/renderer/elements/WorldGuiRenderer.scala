package se.gigurra.wallace.client.stage.world.renderer.elements

import se.gigurra.wallace.client.stage.world.player.PlayerState
import se.gigurra.wallace.client.stage.world.renderer.{RenderAssets, RenderContext}
import se.gigurra.wallace.gamemodel.World

case class WorldGuiRenderer()(implicit renderContext: RenderContext[RenderAssets]) {

  import Renderables._
  import renderContext._

  def render(clientState: PlayerState, worldState: World[_]): Unit = {

    val gdxLogoAsset = assets.sprites.getOrElseUpdate("gdxLogo", assets.libgdxLogo)

    val text = prepText(font = assets.font20, str = s"Fps: ${renderContext.fps}")

    batch {
      transform(_.scalexy(1.0f / 200.0f)) {
        transform(_.center(gdxLogoAsset))(gdxLogoAsset.draw())
        assets.temporary(text) foreach { text => transform(_.center(text))(text.draw()) }
      }
    }
  }

}