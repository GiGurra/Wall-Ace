package se.gigurra.wallace.gamemodel

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag
import scala.language.implicitConversions

case class World[T_TerrainStorage: TerrainStoring](var iSimFrame: WorldSimFrameIndex,
                                                   terrain: Terrain[T_TerrainStorage],
                                                   patch2WorldScale: Int) {

  private val _entities = new ArrayBuffer[Entity]()

  def m2World: Int = patch2WorldScale

  def cm2World: Int = patch2WorldScale / 100

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

  implicit def world2terrain[T_TerrainStorage: TerrainStoring](world: World[T_TerrainStorage]): Terrain[T_TerrainStorage] = world.terrain

  def create[T_TerrainStorage: TerrainStoring](storageFactory: TerrainStorageFactory[T_TerrainStorage],
                                               patchWidth: Int,
                                               patchHeight: Int,
                                               patch2WorldScale: Int = ModelDefaults.patch2WorldScale,
                                               seed: String = "MyMapSeed"): World[T_TerrainStorage] = {
    val terrainStorage = storageFactory.create(patchWidth, patchHeight)
    val out = World(
      iSimFrame = 0L,
      terrain = Terrain(terrainStorage, patch2WorldScale),
      patch2WorldScale = patch2WorldScale
    )
    TerrainGenerator.generate("MyMapSeed", out)
    out
  }

}