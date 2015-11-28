package se.gigurra.wallace.client.gdxbridge

import com.badlogic.gdx
import se.gigurra.wallace.client.Client
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}

class GdxBridge(statCfg: StaticConfiguration,
                dynCfg: DynamicConfiguration) extends gdx.Game {

  lazy val impl = new Client(statCfg, dynCfg)

  override def pause(): Unit = {
    impl.callbacks.onPause()
  }

  override def resume(): Unit = {
    impl.callbacks.onResume()
  }

  override def render(): Unit = {
    impl.callbacks.onUpdate()
  }

  override def dispose(): Unit = {
    impl.callbacks.onExit()
  }

  override def create(): Unit = {
    // Not needed due to lazy impl above
  }
}
