package se.gigurra.wallace.platform.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl.{LwjglCursor, LwjglApplication, LwjglApplicationConfiguration}
import com.badlogic.gdx.graphics.{Pixmap, Cursor}
import org.lwjgl.BufferUtils
import org.lwjgl.input.Mouse
import se.gigurra.wallace.client.Client
import se.gigurra.wallace.client.stage.world.renderer.Sprite
import se.gigurra.wallace.config.client.{Configuration, DynamicConfiguration, StaticConfiguration}

object Main extends App {

  def buildLwjglCfg(statCfg: StaticConfiguration, dynCfg: DynamicConfiguration): LwjglApplicationConfiguration = {
    new LwjglApplicationConfiguration {
      title = statCfg.window_title
      x = dynCfg.window_position.x.toInt
      y = dynCfg.window_position.y.toInt
      width = dynCfg.window_size.x.toInt
      height = dynCfg.window_size.y.toInt
      forceExit = statCfg.app_forceExit
      vSyncEnabled = dynCfg.app_vsyncEnabled
      foregroundFPS = dynCfg.app_foregroundFps
      backgroundFPS = dynCfg.app_backgroundFps
    }
  }

  val statCfg = Configuration.readStatic()
  val dynCfg = Configuration.readDynamic()
  val lwjglCfg = buildLwjglCfg(statCfg, dynCfg)

  new LwjglApplication(new Client(statCfg, dynCfg), lwjglCfg)

}
