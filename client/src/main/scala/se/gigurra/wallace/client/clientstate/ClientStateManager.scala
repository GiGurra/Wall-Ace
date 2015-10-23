package se.gigurra.wallace.client.clientstate

sealed abstract class ClientMainState
case object PreGame extends ClientMainState
case class InGame() extends ClientMainState

object ClientMainState {
  sealed abstract class Modifier
  case object MenuOpen extends Modifier
}

class ClientStateManager {
  var state: ClientMainState = PreGame
  var modifiers: Seq[ClientMainState.Modifier] = Nil
}
