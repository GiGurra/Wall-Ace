package se.gigurra.wallace.gamemodel

trait TerrainStore {

  def width: Int

  def height: Int

  def get(x: Int, y: Int): Terrain

  def set(x: Int, y: Int, value: Terrain): Unit

  def get(pos: WorldVector): Terrain = {
    get(pos.x, pos.y)
  }

  def set(pos: WorldVector, value: Terrain): Unit = {
    set(pos.x, pos.y, value)
  }

  def indexOf(x: Int, y: Int): Int = {
    requireInside(x,y)
    x + y * width
  }

  def requireInside(pos: WorldVector): Unit = {
    requireInside(pos.x, pos.y)
  }

  def requireInside(x: Int, y: Int): Unit = {
    require(x >= 0)
    require(x < width)
    require(y >= 0)
    require(y < height)
  }

}