package se.gigurra.wallace.stage

trait StageManager[InputType] {
  def hasStage(stageId: String): Boolean
  def pushStage(stage: Stage[InputType]): Unit
  def insertBefore(stageId: String, stage: Stage[InputType]): Unit
  def insertAfter(stageId: String, stage: Stage[InputType]): Unit
  def appendStage(stage: Stage[InputType]): Unit
  def removeStage(stageId: String): Unit
}
