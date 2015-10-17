package se.gigurra.wallace.client

import scala.util.Failure
import scala.util.Success
import scala.util.Try

import com.jogamp.newt.event.KeyAdapter
import com.jogamp.newt.event.KeyEvent
import com.jogamp.newt.event.KeyListener
import com.jogamp.newt.event.MouseAdapter
import com.jogamp.newt.event.MouseEvent
import com.jogamp.newt.event.MouseListener
import com.jogamp.newt.opengl.GLWindow

import com.jogamp.opengl.GLCapabilities
import com.jogamp.opengl.GLProfile
import se.gigurra.renderer.Renderer
import se.gigurra.renderer.Widget
import se.gigurra.renderer.glimpl.GlRenderer.Attachable
import se.gigurra.renderer.glimpl.GlRenderer2d
import se.gigurra.util.CallBuffer
import se.gigurra.util.CallBuffer.toInterface
import se.gigurra.util.Mutate.Mutable
import se.gigurra.wallace.GameState

class ClientMain(
  val keyboardInput: CallBuffer[KeyListener],
  val mouseInput: CallBuffer[MouseListener]) extends Widget[ClientMain] {
  val gamestate = new GameState
  val ingameGraphics = new IngameGraphics
  val serverConnection = new ServerConnection

  val mouseHandler = new MouseAdapter {
    override def mouseMoved(e: MouseEvent) {
      println(s"Moved mouse: $e")
    }
  }

  val keyHandler = new KeyAdapter {
    override def keyPressed(e: KeyEvent) {
      println(s"Pressed key: $e")
    }
  }

  override def doDraw(renderer: Renderer, isRoot: Boolean) {

    mouseInput.flush(mouseHandler)
    keyboardInput.flush(keyHandler)

    ingameGraphics.draw(renderer, gamestate.localView.world())

  }

}

object ClientMain {

  def main(args: Array[String]) {

    val glProfile =
      Try(GLProfile.get(GLProfile.GL3)) match {
        case Success(profile) => profile
        case Failure(err) =>
          Try(GLProfile.get(GLProfile.GLES3)) match {
            case Success(profile) => profile
            case Failure(err)     => throw err
          }
      }

    val caps = new GLCapabilities(glProfile).mutate { caps =>
      caps.setNumSamples(4)
      caps.setSampleBuffers(true)
    }

    val glWindow = GLWindow.create(caps).mutate { glWindow =>
      glWindow.setTitle("Wall-Ace")
      glWindow.setSize(1280, 720)
      glWindow.setUndecorated(false)
      glWindow.setPointerVisible(true)
      glWindow.setVisible(true)
    }

    val keyboardInput = new CallBuffer[KeyListener]
    val mouseInput = new CallBuffer[MouseListener]

    glWindow.addKeyListener(keyboardInput)
    glWindow.addMouseListener(mouseInput)

    glWindow.attach(new ClientMain(keyboardInput, mouseInput), new GlRenderer2d(_))

  }
}

