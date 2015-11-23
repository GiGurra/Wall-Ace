package se.gigurra.wallace.gamemodel

trait TerrainStoring[T_TerrainStorage] {
  def width(t: T_TerrainStorage): Int
  def height(t: T_TerrainStorage): Int
  def get(t: T_TerrainStorage, iPatch: Int): TerrainPatch
  def set(t: T_TerrainStorage, iPatch: Int, patch: TerrainPatch): Unit
}

trait TerrainStorageFactory[T_TerrainStorage] {
  def create(wPath: Int, hPath: Int): T_TerrainStorage
}

