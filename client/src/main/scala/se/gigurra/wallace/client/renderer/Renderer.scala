package se.gigurra.wallace.client.renderer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20._
import se.gigurra.wallace.gamemodel.{Terrain, World}

class Renderer {

  import Renderer._
  import Renderables._

  implicit val renderContext = RenderContext(new RenderAssets)

  import renderContext.assets
  import renderContext.drawShortcuts._
  import renderContext.glShortcuts._
  import renderContext.state._

  // End of constructor
  ///////////////////////

  def update[T_Terrain : Rendering] (client_world: World[T_Terrain]): Unit = {

    Projections.ortho11(Gdx.graphics.getWidth, Gdx.graphics.getHeight)
    glClearColor(0.0f, 0.0f, 0.0f, 0)
    glClear(GL_COLOR_BUFFER_BIT)

    batch {
      drawTerrain(client_world.terrain)
      drawGui
    }
  }

  private def drawTerrain[T_Terrain : Rendering](terrain: Terrain[T_Terrain]): Unit = {

    val mapSprite = assets.maps.getOrElseUpdate("mapSprite", terrain)

    mapSprite.upload()

    transform(_
      .unitSize(mapSprite)
      .scalexy(renderContext.maxAspect)
      .center(mapSprite)) {
      mapSprite.draw()
    }
  }

  private def drawGui(): Unit = {

    val gdxLogoAsset = assets.sprites.getOrElseUpdate("gdxLogo", assets.libgdxLogo)

    val text = prepText(font = assets.font20, str = s"Fps: ${renderContext.fps}")
    transform(_.scalexy(1.0f / 400.0f)) {
      transform(_.center(gdxLogoAsset))(gdxLogoAsset.draw())
      transform(_.center(text))(drawText(text))
    }
  }

}

object Renderer {


  implicit class RichRenderable[T: Rendering](val t: T) {
    def buildRenderAsset[AssetsType]()(implicit renderContext: RenderContext[AssetsType]): RenderAsset[T] = {
      implicitly[Rendering[T]].buildRenderAsset(t)(renderContext)
    }
  }

  trait Rendering[Renderable] {
    def buildRenderAsset[AssetsType](renderable: Renderable)(implicit renderContext: RenderContext[AssetsType]): RenderAsset[Renderable]
  }

}