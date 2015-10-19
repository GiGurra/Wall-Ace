package se.gigurra.wallace

import com.badlogic.gdx.backends.lwjgl.{LwjglApplication, LwjglApplicationConfiguration}

object Main extends App {
    val cfg = new LwjglApplicationConfiguration
    cfg.title = "wall-ace"
    cfg.height = 640
    cfg.width = 640
    cfg.forceExit = false
    cfg.vSyncEnabled = true
    cfg.foregroundFPS = 0
    cfg.backgroundFPS = 0
    new LwjglApplication(new Wallace, cfg)
}
