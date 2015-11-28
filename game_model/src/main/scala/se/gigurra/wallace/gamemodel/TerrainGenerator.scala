package se.gigurra.wallace.gamemodel

import scala.util.Random
import scalaxy.streams.optimize

object TerrainGenerator {

  def generate[T_TerrainStorage : TerrainStoring](seed: String, world: World[T_TerrainStorage]): Unit = {

    val rnd = new Random(seed.hashCode)
    val w = world.patchWidth
    val h = world.patchHeight

    optimize {
      for (y <- 0 until h) {
        for (x <- 0 until w) {
          val typ = genType(rnd, x, y, w, h)
          world.setPatch(
            xWorld = x * world.patch2WorldScale,
            yWorld = y * world.patch2WorldScale,
            patch = TerrainPatch.make(rnd, typ))
        }
      }
    }
  }

  private[this] def genType(rnd: Random, x: Int, y: Int, w: Int, h: Int): Byte = {
    // TODO: Impl - Currently SUPER dumb!
    val dyAboveGround = 100 * (h - y) / h
    if (dyAboveGround > 20) {
      TerrainPatch.ALPHA_SPACE
    } else if (dyAboveGround > 10) {
      TerrainPatch.ALPHA_DIRT
    } else {
      TerrainPatch.ALPHA_ROCK
    }
  }

}
