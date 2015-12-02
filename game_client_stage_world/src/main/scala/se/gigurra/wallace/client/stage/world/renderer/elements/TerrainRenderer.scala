package se.gigurra.wallace.client.stage.world.renderer.elements

import se.gigurra.wallace.gamemodel.Terrain
import se.gigurra.wallace.renderer.{Rendering, RenderContext, RenderAssets}

case class TerrainRenderer()(implicit renderContext: RenderContext[RenderAssets]) {

  import Renderables._
  import renderContext._

  def render[T_Terrain: Rendering](terrain: Terrain[T_Terrain]): Unit = {

    val mapSprite = assets.maps.getOrElseUpdate("mapSprite", terrain)

    transform(_.scalexy(terrain.patch2WorldScale)) {
      mapSprite.uploaded.draw()
    }

  }

}
