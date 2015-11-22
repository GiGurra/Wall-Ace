package se.gigurra.wallace.gamemodel

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag
import scala.language.implicitConversions

case class World[T_TerrainStorage <: TerrainStorage](terrain: Terrain[T_TerrainStorage]) {

  private val entities = new ArrayBuffer[Entity]()

  def entities[EntityType <: Entity : ClassTag](pos: WorldVector = WorldVector(),
    maxDelta: Int = 0,
    filter: EntityType => Boolean = (e: EntityType) => true): Seq[EntityType] = {
    terrain.requireInside(pos)
    entities
      .filter(_.isWithin(maxDelta, pos))
      .collect { case e: EntityType => e }
      .filter(filter)
  }

  def addEntity(entity: Entity): Unit = {
    terrain.requireInside(entity.position)
    entities += entity
  }

}

object World {

  implicit def world2terrain[T_TerrainStorage <: TerrainStorage](world: World[T_TerrainStorage]): Terrain[T_TerrainStorage] = world.terrain

  def create[T_TerrainStorage <: TerrainStorage](storageFactory: TerrainStorageFactory[T_TerrainStorage],
                                                 patchWidth: Int,
                                                 patchHeight: Int,
                                                 seed: String = "MyMapSeed"): World[T_TerrainStorage] = {
    val terrainStorage = storageFactory.create(patchWidth, patchHeight)
    val out = World(Terrain(terrainStorage))
    TerrainGenerator.generate("MyMapSeed", out)
    out
  }

}