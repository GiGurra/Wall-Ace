package se.gigurra.wallace.client.toplevelmanagers

import se.gigurra.wallace.config.client.{DynamicConfiguration, StaticConfiguration}
import se.gigurra.wallace.input.InputEvent
import se.gigurra.wallace.stage.{StageInput, StageManager, Stage}

import scala.collection.mutable.ArrayBuffer

case class ClientStageManager(statCfg: StaticConfiguration,
                              dynCfg: DynamicConfiguration)
  extends StageInput[InputEvent]
  with StageManager[InputEvent] {

  private val stages = new ArrayBuffer[Stage[InputEvent]]()

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

  def pushStage(stage: Stage[InputEvent]): Unit = {
    require(!stages.exists(_.stageId == stage.stageId), "Duplicate stage id detected, bailing!")
    stages.insert(0, stage)
    stage.onOpen()
  }

  def insertBefore(stageId: String, stage: Stage[InputEvent]): Unit = {
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

  def insertAfter(stageId: String, stage: Stage[InputEvent]): Unit = {
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

  def appendStage(stage: Stage[InputEvent]): Unit = {
    require(!stages.exists(_.stageId == stage.stageId), "Duplicate stage id detected, bailing!")
    stages += stage
    stage.onOpen()
  }

  def removeStage(stageId: String): Unit = {
    val i = stages.indexWhere(_.stageId == stageId)
    if (i >= 0)
      stages.remove(i).onClose()
  }

  def close(): Unit = {
    stages.reverse.foreach(stage => removeStage(stage.stageId))
  }

}
