package se.gigurra.wallace.client.stage.world

import com.badlogic.gdx.math.Vector3
import se.gigurra.wallace.gamemodel.WorldVector

package object renderer {

  implicit def vec2i2Vec3f(src: WorldVector): Vector3 = new Vector3(src.x.toFloat, src.y.toFloat, 0.0f)

}
