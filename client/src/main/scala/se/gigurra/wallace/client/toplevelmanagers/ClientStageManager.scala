package se.gigurra.wallace.client.toplevelmanagers

import se.gigurra.wallace.client.stage.{MainMenuStage, GameSimulationStage, Stage}
import se.gigurra.wallace.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.input.InputEvent

import scala.collection.mutable.ArrayBuffer

case class ClientStageManager(statCfg: StaticConfiguration,
                              dynCfg: DynamicConfiguration)
  extends TopLevelManager
  with StageManager {

  private val stages = new ArrayBuffer[Stage]()

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

  def hasStage(stageId: String): Boolean = {
    stages.exists(_.stageId == stageId)
  }

  def pushStage(stage: Stage): Unit = {
    require(!stages.exists(_.stageId == stage.stageId), "Duplicate stage id detected, bailing!")
    stages.insert(0, stage)
    stage.onOpen()
  }

  def insertBefore(stageId: String, stage: Stage): Unit = {
    require(!stages.exists(_.stageId == stage.stageId), "Duplicate stage id detected, bailing!")
    val i = stages.indexWhere(_.stageId == stageId)
    if (i >= 0) {
      stages.insert(i, stage)
      stage.onOpen()
    } else {
      stages += stage
      stage.onOpen()
    }
  }

  def insertAfter(stageId: String, stage: Stage): Unit = {
    require(!stages.exists(_.stageId == stage.stageId), "Duplicate stage id detected, bailing!")
    val i = stages.indexWhere(_.stageId == stageId)
    if (i >= 0) {
      stages.insert(i+1, stage)
      stage.onOpen()
    } else {
      stages += stage
      stage.onOpen()
    }
  }

  def appendStage(stage: Stage): Unit = {
    require(!stages.exists(_.stageId == stage.stageId), "Duplicate stage id detected, bailing!")
    stages += stage
    stage.onOpen()
  }

  def removeStage(stageId: String): Unit = {
    val i = stages.indexWhere(_.stageId == stageId)
    if (i >= 0)
      stages.remove(i).onClose()
  }

}
