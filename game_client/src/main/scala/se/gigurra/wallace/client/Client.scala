package se.gigurra.wallace.client

import com.badlogic.gdx.{Game, Gdx}
import se.gigurra.wallace.client.stage.menu.MainMenuStage
import se.gigurra.wallace.client.stage.world.WorldStage
import se.gigurra.wallace.client.toplevelmanagers.{ClientStageManager, ClientWindowManger}
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.input.{InputEvent, InputQue}
import se.gigurra.wallace.stage.StageManager

case class ClientState(statCfg: StaticConfiguration,
                       dynCfg: DynamicConfiguration) {
  val inputQue = InputQue.capture(Gdx.input)
  val clientWindowManager = new ClientWindowManger(statCfg, dynCfg)
  val clientStageManager = new ClientStageManager(statCfg, dynCfg)
  val managers = Seq(clientWindowManager, clientStageManager)
  Client.addDefaultStages(statCfg, dynCfg, clientStageManager)
}

class Client(statCfg: StaticConfiguration,
             dynCfg: DynamicConfiguration) extends Game {

  // Needs to be lazy since gdx wont create our resources before the first render/create callback
  lazy val state = ClientState(statCfg, dynCfg)

  // LibGDX Callbacks
  override def render(): Unit = {
    import state._
    managers.foldLeft(inputQue.pop())((inputs, manager) => manager.consumeInputs(inputs))
    managers.reverse.foreach(_.update())
  }
  override def pause(): Unit = {}
  override def resume(): Unit = {}
  override def dispose(): Unit = {}
  override def create(): Unit = {}
}

object Client {
  def addDefaultStages(statCfg: StaticConfiguration,
                       dynCfg: DynamicConfiguration,
                       stageManager: StageManager[InputEvent]): Unit = {

    stageManager.appendStage(new MainMenuStage(statCfg, dynCfg, stageManager))
    stageManager.appendStage(new WorldStage(statCfg, dynCfg, stageManager))
  }
}
