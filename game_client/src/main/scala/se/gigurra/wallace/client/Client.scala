package se.gigurra.wallace.client

import com.badlogic.gdx.{Game, Gdx}
import se.gigurra.wallace.client.stage.menu.MainMenuStage
import se.gigurra.wallace.client.stage.world.WorldStage
import se.gigurra.wallace.client.toplevelmanagers.{ClientStageManager, ClientWindowManger}
import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.input.{InputEvent, InputQue}
import se.gigurra.wallace.stage.StageManager
import se.gigurra.wallace.util.Tuple2List

import scala.language.implicitConversions

case class ClientState(statCfg: StaticConfiguration,
                       dynCfg: DynamicConfiguration) {

  case class Managers(clientWindowManager: ClientWindowManger = new ClientWindowManger(statCfg, dynCfg),
                      clientStageManager: ClientStageManager = new ClientStageManager(statCfg, dynCfg))

  val inputQue = InputQue.capture(Gdx.input)
  val _managers = Managers()
  val managers = Tuple2List(Managers.unapply(_managers).get).toSeq

  Client.addDefaultStages(statCfg, dynCfg, _managers.clientStageManager)
}

class Client(statCfg: StaticConfiguration,
             dynCfg: DynamicConfiguration) extends Game {

  // Needs to be lazy since gdx wont create our resources before the first render/create callback
  lazy val state = ClientState(statCfg, dynCfg)
  import state._

  // LibGDX Callbacks
  override def render(): Unit = {
    inputQue.pop foreach managers.consume
    managers.reverse.foreach(_.update())
  }
  override def dispose(): Unit = {
    managers.foreach(_.close())
  }
  override def pause(): Unit = {}
  override def resume(): Unit = {}
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
