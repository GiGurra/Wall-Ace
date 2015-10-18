package se.gigurra.wallace

import com.badlogic.gdx.backends.lwjgl.{LwjglApplication, LwjglApplicationConfiguration}

object Main extends App {
    val cfg = new LwjglApplicationConfiguration
    cfg.title = "wall-ace"
    cfg.height = 480
    cfg.width = 800
    cfg.forceExit = false
    cfg.vSyncEnabled = false
    cfg.foregroundFPS = 0
    cfg.backgroundFPS = 0
    new LwjglApplication(new Wallace, cfg)
}
