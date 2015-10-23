package se.gigurra.wallace.gamemodel

trait WorldStore {

  def width: Int

  def height: Int

  def get(x: Int, y: Int): Terrain

  def set(x: Int, y: Int, value: Terrain): Unit

  def set(pos: WorldVector, value: Terrain): Unit = {
    set(pos.x, pos.y, value)
  }

  def terrainAt(pos: WorldVector): Terrain = {
    get(pos.x, pos.y)
  }

  def terrainIndexOf(x: Int, y: Int): Int = {
    requireInsideMap(x,y)
    x + y * width
  }

  def requireInsideMap(pos: WorldVector): Unit = {
    requireInsideMap(pos.x, pos.y)
  }

  def requireInsideMap(x: Int, y: Int): Unit = {
    require(x >= 0)
    require(x < width)
    require(y >= 0)
    require(y < height)
  }

}