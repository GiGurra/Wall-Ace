package se.gigurra.wallace.client

import java.awt.MouseInfo

import se.gigurra.wallace.util.Vec2FixedPoint

case class StaticConfiguration(sim_dt: Int,
                               window_title: String,
                               app_forceExit: Boolean = true)

case class DynamicConfiguration(window_position: Vec2FixedPoint,
                                window_size: Vec2FixedPoint,
                                app_vsyncEnabled: Boolean = true,
                                app_foregroundFps: Int = 0,
                                app_backgroundFps: Int = 0)

object Configuration {

  def readStatic(): StaticConfiguration = {
    StaticConfiguration(
      sim_dt = 20,
      window_title = "wall-ace",
      app_forceExit = true
    )
  }

  def readDynamic(): DynamicConfiguration = {
    val mouseLoc = MouseInfo.getPointerInfo.getLocation
    DynamicConfiguration(
      window_position = Vec2FixedPoint(-1, -1),
      window_size = Vec2FixedPoint(640, 640),
      app_foregroundFps = 0,
      app_backgroundFps = 0
    )
  }

}