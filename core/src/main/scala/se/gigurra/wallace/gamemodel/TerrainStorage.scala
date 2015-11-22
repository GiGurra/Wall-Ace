package se.gigurra.wallace.gamemodel

import java.nio.ByteBuffer

trait TerrainStorage {
  def width: Int
  def height: Int
  def get(iPatch: Int): TerrainPatch
  def set(iPatch: Int, patch: TerrainPatch): Unit
}

trait ByteBufferTerrainStorage extends TerrainStorage {

  def data: ByteBuffer

  override def get(iPatch: Int): TerrainPatch = {
    val offset = iPatch * 4
    TerrainPatch(
      data.get(offset + 0),
      data.get(offset + 1),
      data.get(offset + 2),
      data.get(offset + 3))
  }

  override def set(iPatch: Int, patch: TerrainPatch): Unit = {
    val offset = iPatch * 4
    data
      .put(offset + 0, patch.r)
      .put(offset + 1, patch.g)
      .put(offset + 2, patch.b)
      .put(offset + 3, patch.a)
  }

}

trait TerrainStorageFactory[T_TerrainStorage <: TerrainStorage] {
  def create(wPath: Int, hPath: Int): T_TerrainStorage
}
