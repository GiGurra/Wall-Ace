package se.gigurra.wallace.gamemodel

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag
import scala.language.implicitConversions

case class World[T_TerrainStorage : TerrainStoring](terrain: Terrain[T_TerrainStorage]) {

  private val _entities = new ArrayBuffer[Entity]()

  def entitiesAt[EntityType <: Entity : ClassTag](pos: WorldVector = new WorldVector(),
                                                maxDelta: Int = 0,
                                                filter: EntityType => Boolean = (e: EntityType) => true): Seq[EntityType] = {
    terrain.requireInside(pos)
    _entities
      .filter(_.isWithin(maxDelta, pos))
      .collect { case e: EntityType => e }
      .filter(filter)
  }

  def allEntities: Seq[Entity] = _entities

  def addEntity(entity: Entity): Unit = {
    terrain.requireInside(entity.position)
    _entities += entity
  }

}

object World {

  implicit def world2terrain[T_TerrainStorage : TerrainStoring](world: World[T_TerrainStorage]): Terrain[T_TerrainStorage] = world.terrain

  def create[T_TerrainStorage : TerrainStoring](storageFactory: TerrainStorageFactory[T_TerrainStorage],
                                                 patchWidth: Int,
                                                 patchHeight: Int,
                                                 seed: String = "MyMapSeed"): World[T_TerrainStorage] = {
    val terrainStorage = storageFactory.create(patchWidth, patchHeight)
    val out = World(Terrain(terrainStorage))
    TerrainGenerator.generate("MyMapSeed", out)
    out
  }

}