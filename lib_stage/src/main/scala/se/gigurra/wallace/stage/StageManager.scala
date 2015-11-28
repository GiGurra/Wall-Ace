package se.gigurra.wallace.stage

trait StageManager {
  def hasStage(stageId: String): Boolean
  def pushStage(stage: Stage): Unit
  def insertBefore(stageId: String, stage: Stage): Unit
  def insertAfter(stageId: String, stage: Stage): Unit
  def appendStage(stage: Stage): Unit
  def removeStage(stageId: String): Unit
}
