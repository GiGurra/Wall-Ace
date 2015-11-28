package se.gigurra.wallace.client

import com.badlogic.gdx.Gdx
import se.gigurra.wallace.client.stage.{GameSimulationStage, MainMenuStage}
import se.gigurra.wallace.client.toplevelmanagers.{ClientStageManager, ClientWindowManger}
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.input.InputQue
import se.gigurra.wallace.stage.StageManager

class Client(statCfg: StaticConfiguration,
             dynCfg: DynamicConfiguration) {

  // At the top level we only do the minimum required stuff.
  // Window and Stages are always required.
  val inputQue = InputQue.capture(Gdx.input)
  val clientWindowManager = new ClientWindowManger(statCfg, dynCfg)
  val clientStageManager = new ClientStageManager(statCfg, dynCfg)
  val managers = Seq(clientWindowManager, clientStageManager)

  Client.addDefaultStages(statCfg, dynCfg, clientStageManager)

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
      managers.reverse.foreach(_.update())
    }
  }

}

object Client {

  def addDefaultStages(statCfg: StaticConfiguration,
                       dynCfg: DynamicConfiguration,
                       stageManager: StageManager): Unit = {

    stageManager.appendStage(new MainMenuStage(statCfg, dynCfg, stageManager))
    stageManager.appendStage(new GameSimulationStage(statCfg, dynCfg, stageManager))
  }
}
