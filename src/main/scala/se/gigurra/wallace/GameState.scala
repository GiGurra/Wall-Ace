package se.gigurra.wallace

import se.gigurra.wallace.gamestate.ServerStepMessage
import se.gigurra.wallace.gamestate.World
import se.gigurra.wallace.playerinput.Input

class GameState() {

  private var state = new World
  private var state_predicted: Option[World] = None

  val updater = new IGameUpdater {

    override def update(serverStepMessage: ServerStepMessage) {

      // TODO: Modify state...

      state_predicted = None

    }

    override def simulate(clientInputs: Seq[Input]) {

      // TODO: Modify state...

      state_predicted = None

    }

    override def predict(ownInput: Seq[Input]) {

      // state_predicted = Some(state.deepCopy())

      // TODO: Modify state_predicted...

    }

  }

  val localView = new IGameView {

    override def view(): World = {
      state_predicted.getOrElse(state)
    }

  }

}
