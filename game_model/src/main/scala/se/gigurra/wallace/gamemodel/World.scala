package se.gigurra.wallace.gamemodel

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.language.implicitConversions

case class World[T_TerrainStorage: TerrainStoring](var iSimFrame: WorldSimFrameIndex,
                                                   terrain: Terrain[T_TerrainStorage],
                                                   patch2WorldScale: Int) {

  private val entities = new ArrayBuffer[Entity]()
  private val entityCache = new mutable.HashMap[String, Entity]()

  def getEntity(id: String): Option[Entity] = {
    entityCache.get(id).orElse(entities.find(_.id == id))
  }

  def size: WorldVector = terrain.worldSize

  def m2World: Int = World.m2World(patch2WorldScale)

  def cm2World: Int = World.cm2World(patch2WorldScale)

  def entitiesAt(pos: WorldVector = new WorldVector(),
                 maxDelta: Int = 0,
                 filter: Entity => Boolean = (e: Entity) => true): Seq[Entity] = {
    terrain.requireInside(pos)
    entities
      .filter(_.isWithin(maxDelta, pos))
      .filter(filter)
  }

  def allEntities: Seq[Entity] = entities

  def addEntity(entity: Entity): Unit = {
    terrain.requireInside(entity.position)
    entities += entity
  }

  def flushCaches(): Unit = {
    entityCache.clear()
    entities.foreach(e => entityCache.put(e.id, e))
  }

}

object World {

  implicit def world2terrain[T_TerrainStorage: TerrainStoring](world: World[T_TerrainStorage]): Terrain[T_TerrainStorage] = world.terrain

  def create[T_TerrainStorage: TerrainStoring](storageFactory: TerrainStorageFactory[T_TerrainStorage],
                                               patchWidth: Int,
                                               patchHeight: Int,
                                               patch2WorldScale: Int,
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

  def m2World(patch2WorldScale: Int): Int = patch2WorldScale

  def cm2World(patch2WorldScale: Int): Int = patch2WorldScale / 100

}