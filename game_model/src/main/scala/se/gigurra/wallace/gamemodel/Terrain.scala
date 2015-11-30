package se.gigurra.wallace.gamemodel

case class Terrain[T_TerrainStorage : TerrainStoring](storage: T_TerrainStorage,
                                                      patch2WorldScale: Long) {

  val storing = implicitly[TerrainStoring[T_TerrainStorage]]

  val patchWidth = storing.width(storage)

  val patchHeight = storing.height(storage)

  val worldWidth = patchWidth * patch2WorldScale

  val worldHeight = patchHeight * patch2WorldScale

  val worldSize = WorldVector(worldWidth, worldHeight)

  def getPatch(iPatch: Long): TerrainPatch = storing.get(storage, iPatch)

  def setPatch(iPatch: Long, patch: TerrainPatch): Unit = storing.set(storage, iPatch, patch)

  def setPatch(xWorld: Long, yWorld: Long, patch: TerrainPatch): Unit = {
    setPatch(patchIndexOf(xWorld, yWorld), patch)
  }

  def getPatch(xWorld: Long, yWorld: Long): TerrainPatch = {
    getPatch(patchIndexOf(xWorld, yWorld))
  }

  def getPatch(pos: WorldVector): TerrainPatch = {
    getPatch(pos.x, pos.y)
  }

  def setPatch(pos: WorldVector, value: TerrainPatch): Unit = {
    setPatch(pos.x, pos.y, value)
  }

  def patchIndexOf(pos: WorldVector): Long = {
    patchIndexOf(pos.x, pos.y)
  }

  def patchIndexOf(xWorld: Long, yWorld: Long): Long = {
    requireInside(xWorld, yWorld)
    val xPatch = xWorld / patch2WorldScale
    val yPatch = yWorld / patch2WorldScale
    xPatch + yPatch * patchWidth
  }

  def requireInside(pos: WorldVector): Unit = {
    requireInside(pos.x, pos.y)
  }

  def requireInside(x: Long, y: Long): Unit = {
    require(x >= 0, "x position negative")
    require(x < worldWidth, "x position too large")
    require(y >= 0, "y position negative")
    require(y < worldHeight, "y position too large")
  }

}