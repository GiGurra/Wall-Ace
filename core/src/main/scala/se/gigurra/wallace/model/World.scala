package se.gigurra.wallace.model

import java.nio.ByteBuffer

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

case class World(terrainData: ByteBuffer,
            width: Int,
            height: Int) {
  require(terrainData.capacity == width * height * 4)

  private val entities = new ArrayBuffer[Entity]()

  def entities[EntityType <: Entity : ClassTag](pos: WorldVector = WorldVector(),
                                                maxDelta: Int = math.max(width, height),
                                                filter: EntityType => Boolean = (e: EntityType) => true): Seq[EntityType] = {
    requireInsideMap(pos)
    entities
      .filter(_.isWithin(maxDelta, pos))
      .collect { case e: EntityType => e }
      .filter(filter)
  }

  def addEntity(entity: Entity): Unit = {
    requireInsideMap(entity.position)
    entities += entity
  }

  def setTerrain(pos: WorldVector, color: Terrain): Unit = {
    val i = terrainIndexOf(pos) * 4
    terrainData
      .put(i + 0, color.r)
      .put(i + 1, color.g)
      .put(i + 2, color.b)
      .put(i + 3, color.a)
  }

  def terrainAt(pos: WorldVector): Terrain = {
    val i = terrainIndexOf(pos) * 4
    Terrain(
      terrainData.get(i + 0),
      terrainData.get(i + 1),
      terrainData.get(i + 2),
      terrainData.get(i + 3))
  }

  private[this] def terrainIndexOf(pos: WorldVector): Int = {
    requireInsideMap(pos)
    pos.x + pos.y * width
  }

  private[this] def requireInsideMap(pos: WorldVector): Unit = {
    require(pos.x >= 0)
    require(pos.x < width)
    require(pos.y >= 0)
    require(pos.y < height)
  }

}
