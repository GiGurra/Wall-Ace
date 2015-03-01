package se.gigurra.wallace

import se.gigurra.wallace.gamestate.ServerStepMessage
import se.gigurra.wallace.gamestate.World
import se.gigurra.wallace.playerinput.Input

class GameState() {

  @volatile private var state = new World
  @volatile private var state_predicted = new World

  val updater = new IGameUpdater {

    override def update(serverStepMessage: ServerStepMessage) {

      val world = state.deepCopy()

      // TODO: Modify...

      state = world
      state_predicted = world

    }

    override def simulate(clientInputs: Seq[Input]) {

      val world = state.deepCopy()

      // TODO: Modify...

      state = world
      state_predicted = world

    }

    override def predict(ownInput: Seq[Input]) {

      val world = state.deepCopy()

      // TODO: Modify...

      state_predicted = world
    }

  }

  val view = new IGameView {

    override def view(): World = {
      state_predicted
    }

  }

}
