package se.gigurra.wallace.gamemodel

case class WorldStateManager[T_TerrainStorage <: TerrainStorage](terrainStorageFactory: TerrainStorageFactory[T_TerrainStorage]) {

  val world = World.create(terrainStorageFactory, 640, 640)

  def update(): Unit = {

  }
}
