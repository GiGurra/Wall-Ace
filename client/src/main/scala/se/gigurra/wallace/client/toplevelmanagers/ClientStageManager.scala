package se.gigurra.wallace.client.toplevelmanagers

import se.gigurra.wallace.client.stage.{MainMenuStage, GameSimulationStage, Stage}
import se.gigurra.wallace.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.input.InputEvent

import scala.collection.mutable.ArrayBuffer

case class ClientStageManager(statCfg: StaticConfiguration,
                              dynCfg: DynamicConfiguration) extends TopLevelManager {

  private val stages = new ArrayBuffer[Stage]()

  appendStage(new MainMenuStage(statCfg, dynCfg))
  appendStage(new GameSimulationStage(statCfg, dynCfg))

  /**
    * @param inputs
    * @return Remaining InputEvents that were not consumed
    */
  def consumeInputs(inputs: Seq[InputEvent]): Seq[InputEvent] = {
    stages.foldLeft(inputs)((inputs, stage) => stage.consumeInputs(inputs))
  }

  def update(): Unit = {
    stages.foreach(_.update())
  }

  def pushStage(stage: Stage): Unit = {
    require(!stages.exists(_.id == stage.id), "Duplicate stage id detected, bailing!")
    stages.insert(0, stage)
  }

  def insertBefore(stageId: String, stage: Stage): Unit = {
    require(!stages.exists(_.id == stage.id), "Duplicate stage id detected, bailing!")
    val i = stages.indexWhere(_.id == stageId)
    if (i >= 0) {
      stages.insert(i, stage)
    } else {
      appendStage(stage)
    }
  }

  def insertAfter(stageId: String, stage: Stage): Unit = {
    require(!stages.exists(_.id == stage.id), "Duplicate stage id detected, bailing!")
    val i = stages.indexWhere(_.id == stageId)
    if (i >= 0) {
      stages.insert(i+1, stage)
    } else {
      appendStage(stage)
    }
  }

  def appendStage(stage: Stage): Unit = {
    require(!stages.exists(_.id == stage.id), "Duplicate stage id detected, bailing!")
    stages += stage
  }

  def removeStage(stageId: String): Unit = {
    val i = stages.indexWhere(_.id == stageId)
    if (i >= 0)
      stages.remove(i)
  }

}
