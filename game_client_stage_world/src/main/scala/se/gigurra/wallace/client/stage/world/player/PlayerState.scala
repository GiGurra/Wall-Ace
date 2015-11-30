package se.gigurra.wallace.client.stage.world.player

import java.util.UUID

import se.gigurra.wallace.gamemodel.WorldVector

case class PlayerState(val patch2WorldScale: Int,
                       var unitId: Option[String] = Some(UUID.randomUUID().toString),
                       var cursorWorldPosition: WorldVector = WorldVector(),
                       var cursorHoverUnitId: Option[String] = None) {

  var camera = new Camera(worldPosition = WorldVector(), zoom_distUnitPerScreenUnit = patch2WorldScale * 100)
}
