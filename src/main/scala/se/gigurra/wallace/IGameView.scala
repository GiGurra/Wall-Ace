package se.gigurra.wallace

import se.gigurra.wallace.gamestate.World

trait IGameView {
  
  def world(): World
  
}