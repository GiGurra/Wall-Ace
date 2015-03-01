package se.gigurra.wallace.client

import scala.util.Failure
import scala.util.Success
import scala.util.Try
import com.jogamp.newt.opengl.GLWindow
import javax.media.opengl.GLCapabilities
import javax.media.opengl.GLProfile
import se.gigurra.renderer.Renderer
import se.gigurra.renderer.Widget
import se.gigurra.renderer.glimpl.GlRenderer.Attachable
import se.gigurra.renderer.glimpl.GlRenderer2d
import se.gigurra.util.Mutate.Mutable
import se.gigurra.wallace.GameState
import com.jogamp.newt.event.MouseListener

class ClientMain extends Widget[ClientMain] {

  val gamestate = new GameState
  val ingameGraphics = new IngameGraphics
  val serverConnection = new ServerConnection

  override def doDraw(renderer: Renderer, isRoot: Boolean) {

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
      glWindow.setTitle("Raw GL3ES3 Demo")
      glWindow.setSize(1280, 720)
      glWindow.setUndecorated(false)
      glWindow.setPointerVisible(true)
      glWindow.setVisible(true)
    }
    
    /*
    glWindow.addMouseListener(new MouseListener {
      
    })*/

    glWindow.attach(new ClientMain, new GlRenderer2d(_))

  }
}
