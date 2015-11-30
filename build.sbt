packSettings

val libgdxVersion = "1.7.0"

lazy val baseName = "wall-ace"

lazy val sharedSettings: Seq[Def.Setting[_]] = Seq(
  name := baseName,
  version := "0.1",
  scalaVersion := "2.11.7",
  resolvers += "clojars" at "http://clojars.org/repo",
  libraryDependencies ++= Seq(
    "com.nativelibs4java" %% "scalaxy-streams" % "0.3.4" % "provided",
    "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
    "com.jsuereth" %% "scala-arm" % "1.4"
  ),
  javacOptions ++= Seq(
    "-source", "1.7",
    "-target", "1.7"
  ),
  scalacOptions ++= Seq(
    "-Xlint",
    "-Ywarn-dead-code",
    //"-Ywarn-value-discard",
    "-Ywarn-numeric-widen",
    "-Ywarn-unused",
    "-Ywarn-unused-import",
    "-unchecked",
    "-deprecation",
    "-feature",
    "-encoding", "UTF-8",
    "-target:jvm-1.7"
  ),
  cancelable := true,
  exportJars := true
)

def subproject(name: String): Project = {
  Project(name, file(name))
}

lazy val lib_util = subproject("lib_util") settings (sharedSettings: _*) settings(
  name := baseName + "-lib_util"
)

lazy val game_config = subproject("game_config") settings (sharedSettings: _*) dependsOn lib_util settings(
  name := baseName + "-game_config"
)

lazy val game_model = subproject("game_model") settings (sharedSettings: _*) dependsOn lib_util settings(
  name := baseName + "-game_model"
)

lazy val lib_audio = subproject("lib_audio") settings (sharedSettings: _*) dependsOn lib_util settings(
  name := baseName + "-lib_audio",
  libraryDependencies ++= Seq(
    "com.badlogicgames.gdx" % "gdx" % libgdxVersion
  )
)

lazy val lib_comm = subproject("lib_comm") settings (sharedSettings: _*) dependsOn lib_util settings(
  name := baseName + "-lib_comm",
  libraryDependencies ++= Seq(
    "org.json4s" %% "json4s-native" % "3.3.0",
    "io.reactivex" %% "rxscala" % "0.25.0",
    "com.esotericsoftware" % "kryonet" % "2.22.0-RC1"
  )
)

lazy val lib_input = subproject("lib_input") settings (sharedSettings: _*) dependsOn lib_util settings(
  name := baseName + "-lib_input",
  libraryDependencies ++= Seq(
    "com.badlogicgames.gdx" % "gdx" % libgdxVersion
  )
)

lazy val lib_render = subproject("lib_render") settings (sharedSettings: _*) dependsOn lib_util settings(
  name := baseName + "-lib_render",
  libraryDependencies ++= Seq(
    "com.badlogicgames.gdx" % "gdx" % libgdxVersion,
    "com.badlogicgames.gdx" % "gdx-freetype" % libgdxVersion
  )
)

lazy val lib_stage = subproject("lib_stage") settings (sharedSettings: _*) dependsOn(lib_util) settings(
  name := baseName + "-lib_stage"
)

lazy val game_cursors = subproject("game_cursors") settings (sharedSettings: _*) dependsOn(lib_util, lib_render) settings(
  name := baseName + "-game_cursors"
)

lazy val game_client_stage_menu = subproject("game_client_stage_menu") settings (sharedSettings: _*) dependsOn(lib_util, lib_stage, lib_input, lib_render, lib_audio, game_config, game_cursors) settings(
  name := baseName + "-game_client_stage_menu"
)

lazy val game_client_stage_world = subproject("game_client_stage_world") settings (sharedSettings: _*) dependsOn(lib_util, lib_stage, lib_input, lib_render, lib_audio, game_config, game_cursors, game_model) settings(
  name := baseName + "-game_client_stage_world"
)

lazy val game_client = subproject("game_client") settings (sharedSettings: _*) dependsOn(lib_util, game_client_stage_menu, game_client_stage_world) settings(
  name := baseName + "-game_client"
)

lazy val game_server = subproject("game_server") settings (sharedSettings: _*) dependsOn(lib_util, lib_comm, game_model) settings(
  name := baseName + "-game_server"
)

lazy val platform_desktop = subproject("platform_desktop") settings (sharedSettings: _*) dependsOn game_client settings(
    name := baseName + "-platform_desktop",
    packMain := Map("platform_desktop" -> "se.gigurra.wallace.platform.desktop.Main"),
    libraryDependencies ++= Seq(
      "com.badlogicgames.gdx" % "gdx-backend-lwjgl" % libgdxVersion,
      "com.badlogicgames.gdx" % "gdx-platform" % libgdxVersion classifier "natives-desktop",
      "com.badlogicgames.gdx" % "gdx-freetype-platform" % libgdxVersion classifier "natives-desktop"
    ),
    fork in Compile := true
  )

lazy val all = Project("all", file("."))
  .aggregate(
    lib_util,
    lib_audio,
    lib_comm,
    lib_input,
    lib_render,
    lib_stage,
    game_config,
    game_model,
    game_cursors,
    game_client_stage_menu,
    game_client_stage_world,
    game_client,
    game_server,
    platform_desktop
  )

