package se.gigurra.wallace.client

import com.badlogic.gdx.{Game, ApplicationListener}

class Client extends Game {

  lazy val clientManager = new ClientManager

  override def dispose(): Unit = {
    clientManager.callbacks.onExit()
  }

  override def pause(): Unit = {
    clientManager.callbacks.onPause()
  }

  override def resume(): Unit = {
    clientManager.callbacks.onResume()
  }

  override def render(): Unit = {
    clientManager.callbacks.onUpdate()
  }

  override def create(): Unit = {
  }

}
