package se.gigurra.wallace.model

import scala.util.Random

case class Terrain(r: Byte,
                   g: Byte,
                   b: Byte,
                   a: Byte) {

  import Terrain._

  def is(t: Byte) = a == t

  def isSpace = is(ALPHA_SPACE)

  def isDirt = is(ALPHA_DIRT)

  def isRock = is(ALPHA_ROCK)

  def typ = a

}

object Terrain {

  val ALPHA_SPACE = 255.toByte
  val ALPHA_DIRT = 254.toByte
  val ALPHA_ROCK = 253.toByte

  def apply(r: Int = 0,
            g: Int = 0,
            b: Int = 0,
            a: Byte): Terrain = {

    new Terrain(r.toByte, g.toByte, b.toByte, a)
  }

  def make(rnd: Random, typ: Byte): Terrain = {
    typ match {
      case ALPHA_SPACE => makeSpace(rnd)
      case ALPHA_DIRT => makeDirt(rnd)
      case ALPHA_ROCK => makeRock(rnd)
    }
  }

  def makeSpace(rnd: Random): Terrain = {
    val g = ((rnd.nextDouble * 32.0 + 64.0) / 2.0).toInt
    val r = g * 2
    Terrain(r, g, 0, ALPHA_SPACE)
  }

  def makeDirt(rnd: Random): Terrain = {
    val g = ((rnd.nextDouble * 48.0 + 100.0) / 2.0).toInt
    val r = g * 2
    Terrain(r, g, 0, ALPHA_DIRT)
  }

  def makeRock(rnd: Random): Terrain = {
    val c = (rnd.nextDouble * 100.0 + 128).toInt
    Terrain(c, c, c, ALPHA_ROCK)
  }

}
