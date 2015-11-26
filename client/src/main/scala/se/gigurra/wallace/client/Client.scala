package se.gigurra.wallace.client

import com.badlogic.gdx.Gdx
import se.gigurra.wallace.client.toplevelmanagers.{ClientStageManager, ClientWindowManger, TopLevelManager}
import se.gigurra.wallace.input.InputQue

class Client(statCfg: StaticConfiguration,
             dynCfg: DynamicConfiguration) {

  // At the top level we only do the minimum required stuff.
  // Window and Stages are always required.
  val inputQue = InputQue.capture(Gdx.input)
  val clientWindowManager = new ClientWindowManger(statCfg, dynCfg)
  val clientStageManager = new ClientStageManager(statCfg, dynCfg)
  val managers = Seq(clientWindowManager, clientStageManager)

  object callbacks {

    // Also called just before dispose
    def onPause(): Unit = {
    }

    def onResume(): Unit = {
    }

    def onExit(): Unit = {
    }

    def onUpdate(): Unit = {
      managers.foldLeft(inputQue.pop())((inputs, manager) => manager.consumeInputs(inputs))
      managers.foreach(_.update())
    }
  }

}
