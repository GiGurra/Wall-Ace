package se.gigurra.wallace.client.stage.world.player

import se.gigurra.wallace.gamemodel.WorldVector

case class PlayerState(var ownUnitId: Option[String],
                       var camera: Camera = new Camera(worldPosition = WorldVector()),
                       var cursorWorldPosition: WorldVector = WorldVector(),
                       var cursorHoverUnitId: Option[String] = None)
