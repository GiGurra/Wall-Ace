package se.gigurra.wallace.client.stage.world.clientstate

import se.gigurra.wallace.WorldVector

case class ClientState(var menuOpen: Boolean = false,
                       var camera: Camera = new Camera(worldPosition = WorldVector()))
