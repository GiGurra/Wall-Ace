package se.gigurra.wallace.gamemodel

case class WorldUpdateBatch(iSimFrame: Long, updates: Seq[WorldUpdate])
