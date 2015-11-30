package se.gigurra.wallace.gamemodel

trait TerrainStoring[T_TerrainStorage] {
  def width(t: T_TerrainStorage): Long
  def height(t: T_TerrainStorage): Long
  def get(t: T_TerrainStorage, iPatch: Long): TerrainPatch
  def set(t: T_TerrainStorage, iPatch: Long, patch: TerrainPatch): Unit
}

trait TerrainStorageFactory[T_TerrainStorage] {
  def create(wPath: Long, hPath: Long): T_TerrainStorage
}

