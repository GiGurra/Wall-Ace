package se.gigurra.wallace.gamemodel

case class Terrain[T_TerrainStorage : TerrainStoring](storage: T_TerrainStorage,
                                                      patch2WorldScale: Int = 100) {

  val storing = implicitly[TerrainStoring[T_TerrainStorage]]

  val patchWidth = storing.width(storage)

  val patchHeight = storing.height(storage)

  val worldWidth = patchWidth * patch2WorldScale

  val worldHeight = patchHeight * patch2WorldScale

  def getPatch(iPatch: Int): TerrainPatch = storing.get(storage, iPatch)

  def setPatch(iPatch: Int, patch: TerrainPatch): Unit = storing.set(storage, iPatch, patch)

  def setPatch(xWorld: Int, yWorld: Int, patch: TerrainPatch): Unit = {
    setPatch(patchIndexOf(xWorld, yWorld), patch)
  }

  def getPatch(xWorld: Int, yWorld: Int): TerrainPatch = {
    getPatch(patchIndexOf(xWorld, yWorld))
  }

  def getPatch(pos: WorldVector): TerrainPatch = {
    getPatch(pos.x, pos.y)
  }

  def setPatch(pos: WorldVector, value: TerrainPatch): Unit = {
    setPatch(pos.x, pos.y, value)
  }

  def patchIndexOf(pos: WorldVector): Int = {
    patchIndexOf(pos.x, pos.y)
  }

  def patchIndexOf(xWorld: Int, yWorld: Int): Int = {
    requireInside(xWorld, yWorld)
    val xPatch = xWorld / patch2WorldScale
    val yPatch = yWorld / patch2WorldScale
    xPatch + yPatch * patchWidth
  }

  def requireInside(pos: WorldVector): Unit = {
    requireInside(pos.x, pos.y)
  }

  def requireInside(x: Int, y: Int): Unit = {
    require(x >= 0, "x position negative")
    require(x < worldWidth, "x position too large")
    require(y >= 0, "y position negative")
    require(y < worldHeight, "y position too large")
  }

}