package se.gigurra.wallace.gamemodel

import scala.util.Random
import scalaxy.streams.optimize

object WorldGenerator {

  def generate(seed: String, world: World): Unit = {

    val rnd = new Random(seed.hashCode)
    val w = world.width
    val h = world.height

    optimize {
      for (y <- 0 until h) {
        for (x <- 0 until w) {
          val typ = genType(rnd, x, y, w, h)
          world.set(x, y, Terrain.make(rnd, typ))
        }
      }
    }
  }

  private[this] def genType(rnd: Random, x: Int, y: Int, w: Int, h: Int): Byte = {
    // TODO: Impl - Currently SUPER dumb!
    val dyAboveGround = (h - y).toFloat / h.toFloat
    if (dyAboveGround > 0.2f) {
      Terrain.ALPHA_SPACE
    } else if (dyAboveGround > 0.1f) {
      Terrain.ALPHA_DIRT
    } else {
      Terrain.ALPHA_ROCK
    }
  }

}
