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

  override def consumeInput(input: InputEvent): Option[InputEvent] = {
    stages.consume(input)
  }

  override def update(): Unit = {
    stages.foreach(_.update())
  }

  override def hasStage(stageId: String): Boolean = {
    stages.exists(_.stageId == stageId)
  }

  override def pushStage(stage: Stage[InputEvent]): Unit = {
    require(!stages.exists(_.stageId == stage.stageId), "Duplicate stage id detected, bailing!")
    stages.insert(0, stage)
    stage.onOpen()
  }

  override def insertBefore(stageId: String, stage: Stage[InputEvent]): Unit = {
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

  override def insertAfter(stageId: String, stage: Stage[InputEvent]): Unit = {
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

  override def appendStage(stage: Stage[InputEvent]): Unit = {
    require(!stages.exists(_.stageId == stage.stageId), "Duplicate stage id detected, bailing!")
    stages += stage
    stage.onOpen()
  }

  override def removeStage(stageId: String): Unit = {
    val i = stages.indexWhere(_.stageId == stageId)
    if (i >= 0)
      stages.remove(i).close()
  }

  override def close(): Unit = {
    stages.reverse.foreach(stage => removeStage(stage.stageId))
  }

}
