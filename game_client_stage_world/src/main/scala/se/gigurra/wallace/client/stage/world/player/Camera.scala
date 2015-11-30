package se.gigurra.wallace.client.stage.world.player

import se.gigurra.wallace.gamemodel.WorldVector

case class Camera(var worldPosition: WorldVector,
                  var zoom_distUnitPerScreenUnit: Int) {
  worldPosition = WorldVector(1,1) * zoom_distUnitPerScreenUnit
}
