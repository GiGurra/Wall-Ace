package se.gigurra.wallace.client.stage.world.player

import se.gigurra.wallace.gamemodel.{ModelDefaults, WorldVector}

case class Camera(var worldPosition: WorldVector,
                  var zoom_distUnitPerScreenUnit: Int = ModelDefaults.patch2WorldScale * 100) {
  worldPosition = WorldVector(1,1) * zoom_distUnitPerScreenUnit
}
