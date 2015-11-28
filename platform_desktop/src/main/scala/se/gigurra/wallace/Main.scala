package se.gigurra.wallace

import com.badlogic.gdx.backends.lwjgl.{LwjglApplication, LwjglApplicationConfiguration}
import se.gigurra.wallace.client.gdxbridge.GdxBridge
import se.gigurra.wallace.config.client.Configuration
import se.gigurra.wallace.config.client.{Configuration, DynamicConfiguration, StaticConfiguration}

object Main extends App {

  def buildLwjglCfg(statCfg: StaticConfiguration, dynCfg: DynamicConfiguration): LwjglApplicationConfiguration = {
    new LwjglApplicationConfiguration {
      title = statCfg.window_title
      x = dynCfg.window_position.x
      y = dynCfg.window_position.y
      width = dynCfg.window_size.x
      height = dynCfg.window_size.y
      forceExit = statCfg.app_forceExit
      vSyncEnabled = dynCfg.app_vsyncEnabled
      foregroundFPS = dynCfg.app_foregroundFps
      backgroundFPS = dynCfg.app_backgroundFps
    }
  }

  val statCfg = Configuration.readStatic()
  val dynCfg = Configuration.readDynamic()
  val lwjglCfg = buildLwjglCfg(statCfg, dynCfg)

  new LwjglApplication(new GdxBridge(statCfg, dynCfg), lwjglCfg)
}
