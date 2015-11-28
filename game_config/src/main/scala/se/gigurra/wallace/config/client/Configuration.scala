package se.gigurra.wallace.config.client

import se.gigurra.wallace.util.Vec2FixedPoint

case class StaticConfiguration(sim_dt: Int,
                               window_title: String,
                               app_forceExit: Boolean = true)

case class DynamicConfiguration(var window_position: Vec2FixedPoint,
                                var window_size: Vec2FixedPoint,
                                var app_vsyncEnabled: Boolean = true,
                                var app_foregroundFps: Int = 0,
                                var app_backgroundFps: Int = 0,
                                var game_isSinglePlayer: Boolean = true)

object Configuration {

  def readStatic(): StaticConfiguration = {
    StaticConfiguration(
      sim_dt = 20,
      window_title = "wall-ace",
      app_forceExit = true
    )
  }

  def readDynamic(): DynamicConfiguration = {
    DynamicConfiguration(
      window_position = Vec2FixedPoint(-1, -1),
      window_size = Vec2FixedPoint(640, 640),
      app_foregroundFps = 0,
      app_backgroundFps = 0
    )
  }

}