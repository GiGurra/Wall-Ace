package se.gigurra.wallace

import se.gigurra.wallace.gamestate.World

trait IGameRenderer {

  def draw(world: World)
  
}