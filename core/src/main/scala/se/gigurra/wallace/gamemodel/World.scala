package se.gigurra.wallace.gamemodel

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

case class World(terrain: TerrainStore) {

  private val entities = new ArrayBuffer[Entity]()

  def entities[EntityType <: Entity : ClassTag](pos: WorldVector = WorldVector(),
    maxDelta: Int = math.max(terrain.width, terrain.height),
    filter: EntityType => Boolean = (e: EntityType) => true): Seq[EntityType] = {
    terrain.requireInsideMap(pos)
    entities
      .filter(_.isWithin(maxDelta, pos))
      .collect { case e: EntityType => e }
      .filter(filter)
  }

  def addEntity(entity: Entity): Unit = {
    terrain.requireInsideMap(entity.position)
    entities += entity
  }

}

object World {
  implicit def world2terrain(world: World) = world.terrain
}