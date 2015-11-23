package se.gigurra.wallace.client

import se.gigurra.wallace.client.clientstate.ClientStateManager
import se.gigurra.wallace.client.networkstate.NetworkStateManager
import se.gigurra.wallace.client.renderer.{Renderables, Sprite, Renderer}
import se.gigurra.wallace.gamemodel.WorldStateManager

class Client {
  import Renderables._

  val clientState = new ClientStateManager
  val networkState = new NetworkStateManager
  val worldState = new WorldStateManager(Sprite)
  val renderer = new Renderer

  object callbacks {

    // Also called just before dispose
    def onPause(): Unit = {

    }

    def onResume(): Unit = {

    }

    def onExit(): Unit = {

    }

    def onUpdate(): Unit = {
      // Super simple single threaded design:
      // update network
      // update world
      // update renderer
      clientState.update()
      networkState.update()
      worldState.update()
      renderer.update(worldState.world)
    }
  }
}
