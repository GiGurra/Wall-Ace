package se.gigurra.wallace.client.stage.world.playerstate

import se.gigurra.wallace.WorldVector

case class PlayerState(var ownUnitId: Option[String],
                       var camera: Camera = new Camera(worldPosition = WorldVector()))
